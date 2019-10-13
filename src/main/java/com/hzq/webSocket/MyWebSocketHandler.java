package com.hzq.webSocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hzq.common.Const;
import com.hzq.execption.CustomGenericException;
import com.hzq.common.ServerResponse;
import com.hzq.domain.*;
import com.hzq.service.*;
import com.hzq.vo.GroupMessageAndGroupToUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: blue
 * @Date: 2019/9/28
 * @Description: 创建MyWebSocketHandler类继承WebSocketHandler类
 * （Spring提供的有AbstractWebSocketHandler类、TextWebSocketHandler类、BinaryWebSocketHandler类，
 * 看自己需要进行继承），该类主要是用来处理消息的接收和发送
 * @version: 1.0
 */
@Component
public class MyWebSocketHandler extends AbstractWebSocketHandler implements WebSocketHandler {

    @Autowired
    private MessageService messageService;
    @Autowired
    private GroupMessageContentService groupMessageContentService;
    @Autowired
    private GroupToUserService groupToUserService;
    @Autowired
    private GroupMessageToUserService groupMessageToUserService;
    //日志打印
    private static Logger LOGGER;
    //存储用户的连接
    private static ConcurrentHashMap<Integer, WebSocketSession> USER_SESSION_MAP;

    static {
        LOGGER = LoggerFactory.getLogger(MyWebSocketHandler.class);
        USER_SESSION_MAP = new ConcurrentHashMap<>();
    }

    //WebSocket连接建立后的回调方法
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession){
        int uid = (Integer) webSocketSession.getAttributes().get(Const.CURRENT_CONNECT_ID);
        USER_SESSION_MAP.putIfAbsent(uid, webSocketSession);
    }


    //接收到WebSocket消息后的处理方法
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) {

        if(webSocketMessage.getPayloadLength()==0)
            return ;

        //得到Socket通道中的数据并转化为Content对象
        Content msg = new Gson().fromJson(webSocketMessage.getPayload().toString(), Content.class);

        if (msg.getGroupId() == null) {
            handlerGroupMessage(msg);
        } else {
            handlerUserMessage(msg);
        }

        //发送Socket信息
      //  sendMessageToUser(msg.getMessageToId(), new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
    }

    //WebSocket传输发生错误时的处理方法
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
                System.out.println("WebSocket in staticMap:" + session.getAttributes().get(Const.CURRENT_CONNECT_ID) + "removed");
            }
        }
    }

    //是否处理WebSocket分段消息
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    private void handlerGroupMessage(Content content) {
        GroupMessageContent message = new GroupMessageContent();
            message.setGroupId(content.getGroupId());
            message.setGmFromId(content.getFromId());
            message.setGmContent(content.getMessage());
            message.setGmType(content.getType());
        groupMessageContentService.insert(message);
        Timestamp time = new Timestamp(System.currentTimeMillis());
        ServerResponse<List<GroupMessageAndGroupToUser>> list = groupToUserService.select(content.getGroupId());
        for (GroupMessageAndGroupToUser g : list.getData()) {
            if (USER_SESSION_MAP.get(g.getUserId()) != null) {
                sendMessageToUser(g.getUserId(),new TextMessage(new GsonBuilder().setDateFormat(Const.TIME_FORMAT).create().toJson(message)));
                groupMessageToUserService.update(g.getUserId(),g.getGroupMessageId(),time);
            }
        }
    }

    private void handlerUserMessage(Content content) {
        Integer userId = content.getToId();
        Message message = new Message();
        message.setMessageContent(content.getMessage());
        message.setMessageToId(content.getToId());
        message.setMessageFromId(content.getFromId());
        message.setUserId(content.getToId()); //先帮接收者插入信息
        message.setMessageType(content.getType());
        if (USER_SESSION_MAP.get(userId) == null) {
            message.setMessageStatus(Const.MARK_AS_UNREAD);
        } else {
            message.setMessageStatus(Const.MARK_AS_READ);
            sendMessageToUser(userId,new TextMessage(new GsonBuilder().setDateFormat(Const.TIME_FORMAT).create().toJson(message)));
        }
        messageService.insert(message);
    }

    //发送信息的实现 私发
    private void sendMessageToUser(Integer uid, TextMessage message){
        WebSocketSession session = USER_SESSION_MAP.get(uid);
        try {
            if (session != null && session.isOpen()) {
                session.sendMessage(message);
            }
        } catch (IOException e) {
            throw CustomGenericException.CreateException(40,"发送消息出错");
        }
    }
}