package com.hzq.config;


import com.hzq.interceptors.ChatHandShakeInterceptor;
import com.hzq.handler.ChatWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

/**
 * @Auther: blue
 * @Date: 2019/9/28
 * @Description: springMVC中对websocket的注解配置
 * @version: 1.0
 */
@Configuration
@EnableWebSocket
public class ChatWebSocketConfig implements WebSocketConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWebSocketConfig.class);

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        LOGGER.debug("开始建立连接配置");
        //注册WebSocket处理的类的、及监听/映射路径     //注册WebSocket握手的;
        registry.addHandler(getMyWebSocketHandler(),"/websocket")
                .addInterceptors(new ChatHandShakeInterceptor());
        //注册WebSocket处理的类的、及监听/映射路径      //注册WebSocket握手的拦截器
        registry.addHandler(getMyWebSocketHandler(), "/sockjs/websocket")
                .addInterceptors(getMyHandShakeInterceptor())
                .withSockJS(); //设定支持SockJS

    }


    @Bean
    public ChatWebSocketHandler getMyWebSocketHandler() {
        return new ChatWebSocketHandler();
    }

    @Bean
    public ChatHandShakeInterceptor getMyHandShakeInterceptor() {
        return new ChatHandShakeInterceptor();
    }

}
