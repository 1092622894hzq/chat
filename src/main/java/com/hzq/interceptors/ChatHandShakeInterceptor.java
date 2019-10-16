package com.hzq.interceptors;

import com.hzq.common.Const;
import com.hzq.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Auther: blue
 * @Date: 2019/9/28
 * @Description: 握手拦截器，实现HandshakeInterceptor接口，做一些连接握手或者握手后的一些处理
 * @version: 1.0
 */
public class ChatHandShakeInterceptor implements HandshakeInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatHandShakeInterceptor.class);

    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        LOGGER.debug("握手连接前");
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
//            HttpSession session = servletRequest.getServletRequest().getSession(false);
//            // 标记用户
//            User user = (User) session.getAttribute(Const.CURRENT_USER);
//            LOGGER.debug("Websocket:用户[ID:" + user.getId() + "]已经建立连接");
//            map.put(Const.CURRENT_CONNECT_ID, user.getId());
        return true;
    }

    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
       LOGGER.debug("握手完成后");
    }
}
