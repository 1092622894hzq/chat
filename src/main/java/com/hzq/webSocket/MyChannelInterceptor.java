package com.hzq.webSocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

/**
 * @Auther: blue
 * @Date: 2019/10/8
 * @Description: 一个自定义的ChannelInterceptor可以实现基类的ChannelInterceptorAdapter,
 * 并使用StompHeaderAccessor或SimpMessageHeaderAccessor来访问消息里的信息.
 * @version: 1.0
 */
public class MyChannelInterceptor extends ChannelInterceptorAdapter {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        // ...
        return message;
    }

}
