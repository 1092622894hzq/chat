package com.hzq.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hzq.common.Const;
import com.hzq.dao.GroupToUserDao;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.domain.*;
import com.hzq.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
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
    private GroupMessageContentService groupMessageContentService;
    @Autowired
    private GroupToUserDao groupToUserDao;
    //日志打印
    private static Logger LOGGER;
    //存储用户的连接
    private static ConcurrentHashMap<Integer, WebSocketSession> USER_SESSION_MAP;

    static {
        LOGGER = LoggerFactory.getLogger(ChatWebSocketHandler.class);
        USER_SESSION_MAP = new ConcurrentHashMap<>();
    }

    //WebSocket连接建立后的回调方法
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession){
        int uid = (Integer) webSocketSession.getAttributes().get(Const.CURRENT_CONNECT_ID);
        USER_SESSION_MAP.putIfAbsent(uid, webSocketSession);
        LOGGER.debug("用户已经成功连接了");
        //搜索未登录时接收的消息

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if(message.getPayloadLength()==0) return ;
        try {
            GroupMessageContent content =  new Gson().fromJson(message.getPayload(), GroupMessageContent.class);
            handlerGroupMessage(content);
        } catch (Exception e) {
            Message content =  new Gson().fromJson(message.getPayload(), Message.class);
            handlerUserMessage(content);
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
    }


    //处理群聊消息
    private void handlerGroupMessage(GroupMessageContent message) {
        LOGGER.debug(message.toString());
        groupMessageContentService.insert(message);
        List<GroupToUser>list = groupToUserDao.selectByGroupId(message.getGroupId());
        for (GroupToUser g : list) {
            if (USER_SESSION_MAP.get(g.getUserId()) != null) {
                sendMessageToUser(g.getUserId(),new TextMessage(new GsonBuilder().setDateFormat(Const.TIME_FORMAT).create().toJson(message)));
                GroupToUser groupToUser = new GroupToUser();
                groupToUser.setGroupId(message.getGroupId());
                groupToUser.setUserId(g.getUserId());
                groupToUser.setGroupMessageId(message.getId()); //最新已读消息id
                groupToUserDao.update(groupToUser);
            }
        }
    }

    //处理私聊消息
    private void handlerUserMessage(Message message) {
        LOGGER.debug(message.toString());
        Integer userId = message.getMessageFromId();
        //先帮接收者插入信息
        if (USER_SESSION_MAP.get(userId) == null) {
            message.setMessageStatus(Const.MARK_AS_UNREAD);
        } else {
            message.setMessageStatus(Const.MARK_AS_READ);
            sendMessageToUser(userId,new TextMessage(new GsonBuilder().setDateFormat(Const.TIME_FORMAT).create().toJson(message)));
        }
        messageService.insert(message);
    }

    //发送普通文本信息，私发
    public void sendMessageToUser(Integer uid, TextMessage message){
        WebSocketSession session = USER_SESSION_MAP.get(uid);
        try {
            if (session != null && session.isOpen())
                session.sendMessage(message);
        } catch (IOException e) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"发送消息出错");
        }
    }

    public void sendMessageToUser(Integer uid, Content content) {
        sendMessageToUser(uid,new TextMessage(new GsonBuilder().setDateFormat(Const.TIME_FORMAT).create().toJson(content)));
    }

    //发送文件信息，私发
    private void sendFileMessageToUser(Integer uid, BinaryMessage message) {
        WebSocketSession session = USER_SESSION_MAP.get(uid);
        try {
            if (session != null && session.isOpen())
                session.sendMessage(message);
        } catch (IOException e) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"发送消息出错");
        }
    }

    //判断是否在线
    public boolean isOnline(Integer id) {
        WebSocketSession session = USER_SESSION_MAP.get(id);
        return session != null;
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        LOGGER.debug("连接发生错误");
        if(session.isOpen()){
            session.close();
        }
        for(Map.Entry<Integer, WebSocketSession> entry : USER_SESSION_MAP.entrySet()){
            if(entry.getValue().equals(session)){
                USER_SESSION_MAP.remove(entry.getKey(), session);
            }
        }
    }


    //WebSocket连接关闭后的回调方法
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        LOGGER.debug("用户： " + session.getRemoteAddress() + " is leaving, because:" + closeStatus);
        for (Map.Entry<Integer, WebSocketSession> entry : USER_SESSION_MAP.entrySet()) {
            if (entry.getValue().equals(session)) {
                USER_SESSION_MAP.remove(entry.getKey(), session);
                LOGGER.debug("WebSocket in staticMap:" + session.getAttributes().get(Const.CURRENT_CONNECT_ID) + "removed");
            }
        }
    }

    //是否处理WebSocket分段消息
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}