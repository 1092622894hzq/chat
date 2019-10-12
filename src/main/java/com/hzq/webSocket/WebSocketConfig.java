package com.hzq.webSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * @Auther: blue
 * @Date: 2019/10/7
 * @Description: com.hzq.webSocket
 * @version: 1.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(getMyChannelInterceptor());
    }

    /**
     * 将"/hello"路径注册为STOMP端点，这个路径与发送和接收消息的目的路径有所不同，
     * 这是一个端点，客户端在订阅或发布消息到目的地址前，要连接该端点，
     * 即用户发送请求url="/applicationName/hello"与STOMP server进行连接。之后再转发到订阅url；
     * PS：端点的作用——客户端在订阅或发布消息到目的地址前，要连接该端点。
     */
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        //在网页上可以通过"/applicationName/hello"来和服务器的WebSocket连接
        stompEndpointRegistry.addEndpoint("/hello").withSockJS();
    }

    /**
     * 配置了一个简单的消息代理，如果不重载，默认情况下回自动配置一个简单的内存消息代理，
     * 用来处理以"/topic"为前缀的消息。这里重载configureMessageBroker()方法，
     * 消息代理将会处理前缀为"/topic"和"/queue"的消息。
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //应用程序以/app为前缀，代理目的地以/topic、/user为前缀
        //这句话表示在topic和user这两个域上可以向客户端发消息。
        registry.enableSimpleBroker("/topic", "/user");
        //这句话表示客户单向服务器端发送时的主题上面需要加"/app"作为前缀。
        registry.setApplicationDestinationPrefixes("/app");
        //这句话表示给指定用户发送一对一的主题前缀是"/user"。
        registry.setUserDestinationPrefix("/user");
    }


    @Bean
    public MyChannelInterceptor getMyChannelInterceptor() {
        return new MyChannelInterceptor();
    }

}
