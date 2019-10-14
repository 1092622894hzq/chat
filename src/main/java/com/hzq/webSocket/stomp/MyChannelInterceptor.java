package com.hzq.webSocket.stomp;

import com.hzq.vo.WebsocketUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import java.util.LinkedList;
import java.util.Map;

/**
 * @Auther: blue
 * @Date: 2019/10/8
 * @Description: 一个自定义的ChannelInterceptor可以实现基类的ChannelInterceptorAdapter,
 * 并使用StompHeaderAccessor或SimpMessageHeaderAccessor来访问消息里的信息.
 * @version: 1.0
 */
public class MyChannelInterceptor extends ChannelInterceptorAdapter {

    private static Logger LOGGER = LoggerFactory.getLogger(MyChannelInterceptor.class);



    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
    //    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class); 好像过时了
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message); //从有效负载和给定Message的标头创建一个实例。
        StompCommand command = accessor.getCommand();
        if (StompCommand.COMMIT.equals(command)) {
            LOGGER.debug("聊天连接成功");
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
            if (raw instanceof Map) {
                Object name = ((Map)raw).get("name");
                if (name instanceof LinkedList) {
                    String id = ((LinkedList)name).get(0).toString();
                    //设置当前访问器的认证用户
                    accessor.setUser(new WebsocketUserVo(id));

                }
            }
        }
        return message;
    }

}























