package com.hzq.interceptors;

import com.hzq.common.Const;
import com.hzq.domain.User;
import com.hzq.handler.ChatWebSocketHandler;
import com.hzq.service.UserService;
import com.hzq.vo.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: blue
 * @Date: 2019/9/28
 * @Description: 握手拦截器，实现HandshakeInterceptor接口，做一些连接握手或者握手后的一些处理
 * @version: 1.0
 */
public class ChatHandShakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private UserService userService;
    @Autowired
    private ChatWebSocketHandler chat;

    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
        HttpSession session = servletRequest.getServletRequest().getSession(false);
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        ConcurrentHashMap<Integer, WebSocketSession> sessionMap = chat.getUserSessionMap();
        // 确保得到最新的数据
        user = userService.selectById(user.getId()).getData();
        map.put(Const.CURRENT_USER, user);
        if (sessionMap.get(user.getId()) != null) {
            SendMessage message = new SendMessage(Const.WITHDRAW,null);
            chat.sendMessageToUser(user.getId(),message);
        }
        return true;
    }

    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
    }
}
