package com.hzq.handler;

import com.hzq.common.Const;
import com.hzq.vo.ServerResponse;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.domain.*;
import com.hzq.service.*;
import com.hzq.utils.JsonUtil;
import com.hzq.utils.RedisUtil;
import com.hzq.vo.ApplyVo;
import com.hzq.vo.FriendVo;
import com.hzq.vo.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 创建MyWebSocketHandler类继承WebSocketHandler类
 * （Spring提供的有AbstractWebSocketHandler类、TextWebSocketHandler类、BinaryWebSocketHandler类，
 * 看自己需要进行继承），该类主要是用来处理消息的接收和发送
 * @version: 1.0
 */
@Component("chat")
public class ChatWebSocketHandler extends AbstractWebSocketHandler implements WebSocketHandler {

    @Autowired
    private MessageService messageService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private GroupMessageContentService groupMessageContentService;
    @Autowired
    private GroupToUserService groupToUserService;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisUtil redisUtil;
    //日志打印
    private static Logger LOGGER;
    //存储用户的连接
    private static ConcurrentHashMap<Integer, WebSocketSession> USER_SESSION_MAP;

    static {
        LOGGER = LoggerFactory.getLogger(ChatWebSocketHandler.class);
        USER_SESSION_MAP = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Integer, WebSocketSession> getUserSessionMap(){
        return USER_SESSION_MAP;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        User user = (User) session.getAttributes().get(Const.CURRENT_USER);
        Integer uid = user.getId();
        USER_SESSION_MAP.put(uid, session);
        //处理用户在线的标志位
        if (!user.getStatus().equals(Const.INVISIBLE)) {
            userService.updateStatus(Const.ONLINE, uid);
        } else {
            userService.updateStatus(Const.INVISIBLE,uid);
        }
        //处理redis中的消息
        String pattern = "*-" + uid + "-*";
        if (redisUtil.existsKeys(pattern)) {
            List<SendMessage> list = redisUtil.getValues(pattern);
            for (SendMessage m : list) {
                sendMessageToUser(uid,m);
            }
            redisUtil.removePattern(pattern);
        }
        //1.私聊消息
        ServerResponse<List<SendMessage>> response = messageService.selectUnReadSendMessage(uid);
        if (response.isSuccess()) {
            List<SendMessage> messageList = response.getData();
            //发送消息
            for (SendMessage m : messageList) {
                m.setType(Const.PRIVATE_CHAT);
                sendMessageToUser(uid,m);
            }
            //更新消息状态
            messageService.update(messageList);
        }
        //2.群聊消息
        response = groupMessageContentService.selectAllUnread(uid);
        //发送消息
        if (response.isSuccess()) {
            List<SendMessage> list = response.getData();
            for (SendMessage g : list) {
                g.setType(Const.GROUP_CHAT);
                sendMessageToUser(uid,g);
            }
            //更新未读消息
            groupMessageContentService.update(list,uid);
        }
        //3.心跳
        Heart heart = new Heart(session);
        Thread thread = new Thread(heart);
        thread.start();
        session.getAttributes().put(Const.CURRENT_HEART,heart);
        session.getAttributes().put(Const.CURRENT_THREAD,thread);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        LOGGER.debug("用户正常离开了, 因为:" + closeStatus);
        //结束心跳
        stopHeart(session);
        //移除session
        removeSession(session);
    }

    @Override
    protected  void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (message.getPayloadLength() == 0) return;
        SendMessage content = JsonUtil.getObjFromJson(message.getPayload(), SendMessage.class);
        if (!content.getType().equals(Const.HEART)) {
            LOGGER.debug("接收到消息" + content.toString());
        }
        //心跳消息
        if (content.getType().equals(Const.HEART)) {
            sendMessageToUser(content.getFromId(), content);
        }
        //提前结束心跳
        ((Heart)session.getAttributes().get(Const.CURRENT_HEART)).setRemove(false);
        ((Thread)session.getAttributes().get(Const.CURRENT_THREAD)).interrupt();
        //私聊
        if (content.getType().equals(Const.PRIVATE_CHAT)) {
            Integer fromId = content.getFromId();
            Integer toId = content.getToIdOrGroupId();
            //发送给本人
            messageService.insert(content,Const.MARK_AS_READ,fromId);
            sendMessageToUser(fromId,content);
            //发送给好友
            if (USER_SESSION_MAP.get(toId) == null) {
                cacheMessage(content, toId);
            } else {
                messageService.insert(content,Const.MARK_AS_READ,toId);
                sendMessageToUser(toId,content);
            }
        }
        //群聊
        if (content.getType().equals(Const.GROUP_CHAT)) {
            groupMessageContentService.insert(content);
            List<GroupToUser> list = groupToUserService.selectByGroupId(content.getToIdOrGroupId()).getData();
            for (GroupToUser g : list) {
                if (USER_SESSION_MAP.get(g.getUserId()) != null ) {
                    sendMessageToUser(g.getUserId(),content);
                    GroupToUser groupToUser = new GroupToUser(g.getUserId(),content.getToIdOrGroupId(),content.getId());
                    groupToUserService.update(groupToUser);
                } else {
                    cacheMessage(content,g.getUserId());
                }
            }
        }
    }

    //私发
    public void sendMessageToUser(Integer uid, SendMessage message) {
        TextMessage msg = new TextMessage(JsonUtil.toJson(message));
        WebSocketSession session = USER_SESSION_MAP.get(uid);
        try {
            if (session != null && session.isOpen()) {
                session.sendMessage(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(), "发送消息出错");
        }
    }

    //系统通知好友申请消息
    public void systemAdviceApply(Apply apply) {
        //获取申请人信息
        Integer toId = apply.getToId();
        ApplyVo applyVo = applyService.select(apply.getFromId(), toId).getData();
        SendMessage message = new SendMessage(Const.APPLY, JsonUtil.toJson(applyVo));
        //在线就发送消息，不在线就存入redis中
        if (USER_SESSION_MAP.get(toId) == null) {
            cacheMessage(message, toId);
        } else {
            sendMessageToUser(toId,message);
        }
    }

    //系统通知好友添加成功消息
    public void systemAdviceFriend(Integer friendId, Integer userId) {
        //告诉安卓，好友添加成功
        SendMessage message = new SendMessage();
        FriendVo friendVo = friendService.selectFriendByFriendId(friendId,userId).getData();
        message.setMessage(JsonUtil.toJson(friendVo));
        message.setType(Const.FRIEND_ADD);
        message.setMessageType(Const.TEXT);
        message.setGmtCreate(new Timestamp(System.currentTimeMillis()));
        message.setFromId(friendId);
        message.setToIdOrGroupId(userId);
        FriendVo friend = friendService.selectFriendByFriendId(userId, friendId).getData();
        message.setAvatar(friend.getAvatar());
        message.setName(friend.getFriendName());
        //发送给自己
        sendMessageToUser(userId,message);
        //发送给好友
        message.setMessage(JsonUtil.toJson(friend));
        message.setFromId(userId);
        message.setToIdOrGroupId(friendId);
        friend = friendService.selectFriendByFriendId(friendId, userId).getData();
        message.setAvatar(friend.getAvatar());
        message.setName(friend.getFriendName());
        if (USER_SESSION_MAP.get(friendId) == null) {
            cacheMessage(message, friendId);
        } else {
            sendMessageToUser(friendId,message);
            messageService.insert(message, Const.MARK_AS_READ, friendId);
        }
    }

    //系统通知添加群成员
    public void systemAdviceGroupMember(Integer groupId, Integer userId, Integer type) {
        UserInfo userInfo = userInfoService.queryUserById(userId).getData();
        SendMessage message = new SendMessage();
        if (type.equals(Const.GROUP_ADD_MEMBER)) {
            message.setMessage("我" + userInfo.getNickname() + "加入群聊");
        }
        message.setType(type);
        message.setMessageType(Const.TEXT);
        message.setGmtCreate(new Timestamp(System.currentTimeMillis()));
        message.setToIdOrGroupId(groupId);
        message.setName(userInfo.getNickname());
        message.setAvatar(userInfo.getAvatar());
        List<GroupToUser> list = groupToUserService.selectByGroupId(groupId).getData();
        for (GroupToUser g : list) {
            if (g.getUserId().equals(userId)) {
                message.setFromId(g.getId());
            }
        }
        for (GroupToUser g : list) {
            if (USER_SESSION_MAP.get(g.getUserId()) != null ) {
                sendMessageToUser(g.getUserId(),message);
            } else {
                cacheMessage(message,g.getUserId());
            }
        }
    }

    //设置每个用户限存两百条未读消息
    private void cacheMessage(SendMessage message, Integer toId) {
        String pattern = "*-" + toId + "-*";
        Integer size = redisUtil.getKeySize(pattern);
        if (message.getType().equals(Const.APPLY) || message.getType().equals(Const.GROUP_ADD_MEMBER)) { //申请通知就不存入数据库
            String key = message.getId() + "-" + toId + "-" + message.getId();
            redisUtil.set(key, JsonUtil.toJson(message));
        }
        String key = message.getId() + "-" + toId + "-" + message.getId();
        if (size < Const.MAX_SIZE && message.getType().equals(Const.PRIVATE_CHAT)) {
            messageService.insert(message, Const.MARK_AS_READ, toId);
            redisUtil.set(key, JsonUtil.toJson(message));
        }
        if (size > Const.MAX_SIZE && message.getType().equals(Const.PRIVATE_CHAT)){
            messageService.insert(message, Const.MARK_AS_UNREAD, toId);
        }
        if (size < Const.MAX_SIZE && message.getType().equals(Const.GROUP_CHAT)) {
            redisUtil.set(key, JsonUtil.toJson(message));
            GroupToUser groupToUser = new GroupToUser(toId,message.getToIdOrGroupId(),message.getId());
            groupToUserService.update(groupToUser);
        }
    }

    //移除session
    private void removeSession(WebSocketSession session) {
        for (Map.Entry<Integer, WebSocketSession> entry : USER_SESSION_MAP.entrySet()) {
            if (entry.getValue().equals(session)) {
                USER_SESSION_MAP.remove(entry.getKey(), session);
                try {
                    if (session.isOpen()) {
                        session.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //处理用户离线的标志位
                User user = (User) session.getAttributes().get(Const.CURRENT_USER);
                if (user.getStatus().equals(Const.ONLINE)) {
                    userService.updateStatus(Const.OFFLINE,entry.getKey());
                } else {
                    userService.updateStatus(Const.INVISIBLE,entry.getKey());
                }
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) {
        LOGGER.debug("连接发生错误: "+throwable.getCause()+"--"+throwable.getMessage()+"---"+ Arrays.toString(throwable.getStackTrace()));
        //结束心跳
        stopHeart(session);
        //移除session
        removeSession(session);
    }

    private void stopHeart(WebSocketSession session) {
        Heart heart = (Heart) session.getAttributes().get(Const.CURRENT_HEART);
        if (heart != null) {
            heart.setFlag(false);
            heart.setRemove(false);
        } else {
            System.out.println("session不存在heart");
        }
    }

    class Heart implements Runnable {
        private boolean flag = true;
        private boolean remove = true;
        private WebSocketSession session;

        @Override
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("提前结束休眠");
                }
                if (remove) {
                    removeSession(session);
                }
                remove = true;
            }
        }

        Heart(WebSocketSession session){
            this.session = session;
        }

        public Heart(){

        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public boolean isRemove() {
            return remove;
        }

        public void setRemove(boolean remove) {
            this.remove = remove;
        }

        public WebSocketSession getSession() {
            return session;
        }

        public void setSession(WebSocketSession session) {
            this.session = session;
        }
    }
}

