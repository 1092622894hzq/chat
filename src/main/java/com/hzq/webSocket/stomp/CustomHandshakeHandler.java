package com.hzq.webSocket.stomp;

import com.hzq.vo.WebsocketUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(CustomHandshakeHandler.class);

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        for (String key : attributes.keySet()) {
            LOGGER.debug(attributes.get(key).toString());
        }
        return new WebsocketUserVo(UUID.randomUUID().toString());
    }
}
