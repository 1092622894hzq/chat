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
import com.hzq.vo.CommonResult;
import com.hzq.vo.FriendVo;
import com.hzq.vo.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        int uid = (Integer) webSocketSession.getAttributes().get(Const.CURRENT_CONNECT_ID);
        USER_SESSION_MAP.put(uid, webSocketSession);
        //处理用户在线的标志位
        userService.updateStatus(Const.ONLINE,uid);
        //处理离线消息
        //处理redis中的消息
        Object msg = redisUtil.get(String.valueOf(uid));
        if (msg != null) {
            CommonResult result = JsonUtil.getObjFromJson((String) msg, CommonResult.class);
            List<SendMessage> messageList = result.getContentList();
            if (messageList != null && !messageList.isEmpty()) {
                for (SendMessage m : messageList) {
                    sendMessageToUser(uid,getTextMessage(m));
                }
            }
            if (redisUtil.exists(String.valueOf(uid))) {
                redisUtil.remove(String.valueOf(uid));
            }
        }
        //1.私聊消息
        ServerResponse<List<SendMessage>> response = messageService.selectUnReadSendMessage(uid);
        if (response.isSuccess()) {
            List<SendMessage> messageList = response.getData();
            LOGGER.debug("一共有多少条离线消息："+messageList.size());
            //发送消息
            for (SendMessage m : messageList) {
                sendMessageToUser(uid,getTextMessage(m));
            }
            System.out.println(messageList.get(0).getId()+"---"+messageList.get(messageList.size()-1).getId());
            //更新消息状态
            if (messageList.size() == 1) {
                messageService.updateOneMessage(messageList.get(0).getId());
            } else {
                Integer bigId = messageList.get(messageList.size()-1).getId();
                Integer smallId = messageList.get(0).getId();
                messageService.update(bigId,smallId,uid);
            }
        }

        //2.群聊消息

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        LOGGER.debug("用户正常离开了, 因为:" + closeStatus);
        LOGGER.debug("WebSocket in staticMap: 用户id为：" + session.getAttributes().get(Const.CURRENT_CONNECT_ID) + " 离开了");
        //移除session
        removeSession(session);
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
        LOGGER.debug("进入消息处理器中");
        if(message.getPayloadLength()==0) return ;
        try {
            SendMessage content =  JsonUtil.getObjFromJson(message.getPayload(), SendMessage.class);
            content.setGmtCreate(new Timestamp(System.currentTimeMillis()));
            if (content.getType().equals(Const.PRIVATE_CHAT)) {
                handlerUserMessage(content);
            } else {
                handlerGroupMessage(content);
            }
            LOGGER.debug("处理消息成功");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("消息格式不对");
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message){
        if (message.getPayloadLength() == 0) return;
        ByteBuffer buffer = message.getPayload();

    }




    //处理群聊消息
    private void handlerGroupMessage(SendMessage message) {
        LOGGER.debug("群聊消息内容："+message.toString());
        groupMessageContentService.insert(message);
        List<GroupToUser> list = groupToUserDao.selectByGroupId(message.getToIdOrGroupId());
        for (GroupToUser g : list) {
            if (USER_SESSION_MAP.get(g.getUserId()) != null) {
                sendMessageToUser(g.getUserId(),getTextMessage(message));
                GroupToUser groupToUser = new GroupToUser();
                groupToUser.setGroupId(message.getToIdOrGroupId());
                groupToUser.setUserId(g.getUserId());
                groupToUser.setGroupMessageId(message.getToIdOrGroupId()); //最新已读消息id
                groupToUserDao.update(groupToUser);
            }
        }
    }

    //处理私聊消息
    private void handlerUserMessage(SendMessage message) {
        LOGGER.debug("私聊消息的内容 :"+message.toString());
        Integer fromId = message.getFromId();
        Integer toId = message.getToIdOrGroupId();
        messageService.insert(message,Const.MARK_AS_READ,fromId);
        LOGGER.debug("开始发送给自己");
        sendMessageToUser(fromId,getTextMessage(message));  //返回主键
        LOGGER.debug("开始发送别人");
        if (USER_SESSION_MAP.get(toId) == null) {
            messageService.insert(message,Const.MARK_AS_UNREAD,toId);
        } else {
            messageService.insert(message,Const.MARK_AS_READ,toId);
            sendMessageToUser(toId,getTextMessage(message));
        }
        LOGGER.debug("发送成功");
    }

    //转换消息
    private TextMessage getTextMessage(SendMessage message) {
        return new TextMessage(new GsonBuilder().setDateFormat(Const.TIME_FORMAT).create().toJson(message));
    }

    //发送普通文本信息，私发
    private void sendMessageToUser(Integer uid, TextMessage message){
        WebSocketSession session = USER_SESSION_MAP.get(uid);
        try {
            if (session != null && session.isOpen())
                session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.debug("发送消息失败");

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
            redisUtil.appendObj(toId.toString(),message);
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
            messageService.insert(message,Const.MARK_AS_UNREAD,friendId);
        } else {
            sendMessageToUser(friendId,getTextMessage(message));
            messageService.insert(message,Const.MARK_AS_READ,friendId);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable){
        LOGGER.debug("连接发生错误: "+throwable.getCause()+"--"+throwable.getMessage()+"---"+ Arrays.toString(throwable.getStackTrace()));
        try {
            if(session.isOpen()){
                session.close();
            }
            LOGGER.debug("发生错误之后，成功关闭连接，并移除连接");
        } catch (IOException e) {
            LOGGER.debug("关闭的时候发生错误");
            e.printStackTrace();
        }
        //移除session
        removeSession(session);
    }

    //移除session
    private void removeSession(WebSocketSession session) {
        for(Map.Entry<Integer, WebSocketSession> entry : USER_SESSION_MAP.entrySet()){
            if(entry.getValue().equals(session)){
                USER_SESSION_MAP.remove(entry.getKey(), session);
                //处理用户离线的标志位
                userService.updateStatus(Const.OFFLINE,entry.getKey());
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}