package com.hzq.handler;

import com.google.gson.GsonBuilder;
import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.dao.GroupToUserDao;
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
    private GroupToUserDao groupToUserDao;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserService userService;

    private static boolean flag = true;
    private static boolean remove = true;
    private static Thread thread;
    //日志打印
    private static Logger LOGGER;
    //存储用户的连接
    private static ConcurrentHashMap<Integer, WebSocketSession> USER_SESSION_MAP;

    static {
        LOGGER = LoggerFactory.getLogger(ChatWebSocketHandler.class);
        USER_SESSION_MAP = new ConcurrentHashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession){
        Integer uid = (Integer) webSocketSession.getAttributes().get(Const.CURRENT_CONNECT_ID);
        USER_SESSION_MAP.put(uid, webSocketSession);
        //处理用户在线的标志位
        userService.updateStatus(Const.ONLINE,uid);
        //处理离线消息
        //处理redis中的消息
        String pattern = "*-"+uid+"-*";
        if (redisUtil.existsKeys(pattern)) {
            List<SendMessage> list = redisUtil.getValues(pattern);
            for (SendMessage m : list) {
                sendMessageToUser(uid,getTextMessage(m));
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
                sendMessageToUser(uid,getTextMessage(m));
            }
            //更新消息状态
            messageService.update(messageList);
            }
        //2.群聊消息

        //3.心跳
        changeHear(true);
        thread = new Thread(()->{
            while (flag) {
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    //System.out.println("提前结束休眠");
                }
                if (remove) {
                    removeSession(webSocketSession);
                }
                remove = true;
            }
        });
        thread.start();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        //LOGGER.debug("用户正常离开了, 因为:" + closeStatus);
        //结束心跳
        changeHear(false);
        //移除session
        removeSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        if(message.getPayloadLength()==0) return ;
        SendMessage content =  JsonUtil.getObjFromJson(message.getPayload(), SendMessage.class);
        content.setGmtCreate(new Timestamp(System.currentTimeMillis()));
        if (!content.getType().equals(Const.HEART)) {
            LOGGER.debug("接收到消息"+content.toString());
        }
        if (content.getType().equals(Const.HEART)) {
            //心跳消息
            sendMessageToUser(content.getFromId(),message);
            //LOGGER.debug("接收到pong消息");
            endSleep();
            return;
        }
        if (content.getType().equals(Const.PRIVATE_CHAT)) {
            //私聊
            Integer fromId = content.getFromId();
            Integer toId = content.getToIdOrGroupId();
            //发送给本人
            messageService.insert(content,Const.MARK_AS_READ,fromId);
            sendMessageToUser(fromId,getTextMessage(content));
            //发送给好友
            if (USER_SESSION_MAP.get(toId) == null || !USER_SESSION_MAP.get(toId).isOpen()) {
                cacheMessage(content,toId);
            } else {
                messageService.insert(content,Const.MARK_AS_READ,toId);
                sendMessageToUser(toId,getTextMessage(content));
            }
        }
        if (content.getType().equals(Const.GROUP_CHAT)) {
            //群聊
            groupMessageContentService.insert(content);
            List<GroupToUser> list = groupToUserDao.selectByGroupId(content.getToIdOrGroupId());
            for (GroupToUser g : list) {
                if (USER_SESSION_MAP.get(g.getUserId()) != null && USER_SESSION_MAP.get(g.getUserId()).isOpen()) {
                    sendMessageToUser(g.getUserId(),getTextMessage(content));
                    GroupToUser groupToUser = new GroupToUser();
                    groupToUser.setGroupId(content.getToIdOrGroupId());
                    groupToUser.setUserId(g.getUserId());
                    groupToUser.setGroupMessageId(content.getToIdOrGroupId()); //最新已读消息id
                    groupToUserDao.update(groupToUser);
                }
            }
        }
    }

    //私发
    private void sendMessageToUser(Integer uid, TextMessage message) {
        WebSocketSession session = USER_SESSION_MAP.get(uid);
        try {
            if (session != null && session.isOpen()) {
                session.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"发送消息出错");
        }
    }

    //系统通知好友申请消息
    public void systemAdviceApply(Apply apply) {
        //获取申请人信息
        Integer toId = apply.getToId();
        ApplyVo applyVo = applyService.select(apply.getFromId(),toId).getData();
        SendMessage message = new SendMessage(Const.APPLY,JsonUtil.toJson(applyVo));
        //在线就发送消息，不在线就存入redis中
        if (USER_SESSION_MAP.get(toId) == null) {
            cacheMessage(message,toId);
        } else {
            sendMessageToUser(toId,getTextMessage(message));
        }
    }

    //系统通知好友添加成功消息
    public void systemAdviceFriend(Integer friendId, Integer userId){
        //告诉安卓，好友添加成功
        SendMessage message = new SendMessage();
        message.setMessage("我们已经是朋友了，可以开始聊天了");
        message.setType(Const.PRIVATE_CHAT);
        message.setMessageType(Const.TEXT);
        message.setGmtCreate(new Timestamp(System.currentTimeMillis()));
        message.setFromId(friendId);
        message.setToIdOrGroupId(userId);
        FriendVo friend = friendService.selectFriendByFriendId(userId,friendId).getData();
        message.setAvatar(friend.getAvatar());
        message.setName(friend.getFriendName());
        //发送给自己
        sendMessageToUser(userId,getTextMessage(message));
        //发送给好友
        message.setMessage("我通过了你的朋友验证请求，现在我们可以开始聊天了");
        message.setFromId(userId);
        message.setToIdOrGroupId(friendId);
        friend = friendService.selectFriendByFriendId(friendId,userId).getData();
        message.setAvatar(friend.getAvatar());
        message.setName(friend.getFriendName());
        if (USER_SESSION_MAP.get(friendId) == null) {
            cacheMessage(message,friendId);
        } else {
            sendMessageToUser(friendId,getTextMessage(message));
            messageService.insert(message,Const.MARK_AS_READ,friendId);
        }
    }

    //设置每个用户限存两百条未读消息
    private void cacheMessage(SendMessage message, Integer toId) {
        String pattern = "*-"+toId+"-*";
        Integer size = redisUtil.getKeySize(pattern);
        if (size < Const.MAX_SIZE) {
            String key = message.getId() + "-" + toId + "-" + message.getId();
            redisUtil.set(key, JsonUtil.toJson(message));
            messageService.insert(message, Const.MARK_AS_READ, toId);
        } else {
            messageService.insert(message,Const.MARK_AS_UNREAD,toId);
        }
    }

    //移除session
    private void removeSession(WebSocketSession session) {
        for(Map.Entry<Integer, WebSocketSession> entry : USER_SESSION_MAP.entrySet()){
            if(entry.getValue().equals(session)){
                USER_SESSION_MAP.remove(entry.getKey(), session);
                try {
                    if (session.isOpen()) {
                        session.close();
                    }
                } catch (IOException e) {
                    LOGGER.debug("关闭的时候发生错误");
                    e.printStackTrace();
                }
                //处理用户离线的标志位
                userService.updateStatus(Const.OFFLINE,entry.getKey());
            }
        }
    }

    //转换消息类型
    private TextMessage getTextMessage(SendMessage message) {
        return new TextMessage(new GsonBuilder().setDateFormat(Const.TIME_FORMAT).create().toJson(message));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable){
        //LOGGER.debug("连接发生错误: "+throwable.getCause()+"--"+throwable.getMessage()+"---"+ Arrays.toString(throwable.getStackTrace()));
        //移除session
        removeSession(session);
    }

    //设置心跳结束的默认值--false 设置心跳开始的默认值--true
    private void changeHear(boolean isTrue) {
        flag = isTrue;
        remove = isTrue;
    }
    //设置中断睡眠的默认值
    private void endSleep() {
        remove = false;
        thread.interrupt();
    }

}