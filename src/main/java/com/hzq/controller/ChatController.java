package com.hzq.controller;

import com.hzq.common.CustomGenericException;
import com.hzq.common.ServerResponse;
import com.hzq.domain.Content;
import com.hzq.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Map;
 

@RestController
public class ChatController {
 
    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;
    @Autowired
    private WebSocketService webSocketService;
 
    /**
     * 表示服务端可以接收客户端通过主题“/app/hello”发送过来的消息，客户端需要在主题"/topic/hello"上监听并接收服务端发回的消息
     * @param topic 内容主题   greetings
     * @param headers  {simpMessageType=MESSAGE, stompCommand=SEND, nativeHeaders={destination=[/app/hello], content-length=[18], atytopic=[greetings]}, DestinationVariableMethodArgumentResolver.templateVariables={}, simpSessionAttributes={}, lookupDestination=/hello, simpSessionId=deovbx2n, simpDestination=/app/hello, id=cc6b83a1-54a9-b025-d6c1-9c49e81951bd, timestamp=1570536487294}
     */
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(Content msg, @Header("atytopic") String topic, @Headers Map<String, Object> headers) {
        System.out.println("connected successfully....");
        System.out.println("Content: "+msg.getMessage());
        System.out.println(topic);
        System.out.println(headers);
        Timestamp time = new Timestamp((Long) headers.get("timestamp"));
        System.out.println("time:"+time+"---"+headers.get("timestamp"));
        return msg.toJson();
      //  webSocketService.sendChatMessage(m);
    }
 
    /**
     * 这里用的是@SendToUser，这就是发送给单一客户端的标志。本例中，
     * 客户端接收一对一消息的主题应该是“/user/” + 用户Id + “/message” ,这里的用户id可以是一个普通的字符串，只要每个用户端都使用自己的id并且服务端知道每个用户的id就行。
     * @return 返回通用对象
     */
    @MessageMapping("/message")
    @SendToUser("/{userId}/message")
    public String handleSubscribe(Content msg) {
        System.out.println("this is the @SubscribeMapping('/marco')");
     //   simpMessageSendingOperations.convertAndSendToUser();
        return msg.toJson();

    }
    /*

    使用Spring和STOMP消息功能的时候，我们有三种方式利用认证用户：
    @MessageMapping和@SubscribeMapping标注的方法能够使用Principal来获取认证用户；
    @MessageMapping、@SubscribeMapping和@MessageException方法返回的值能够以消息的形式发送给认证用户；
    SimpMessagingTemplate能够发送消息给特定用户。
    @SubscribeMapping("/marco") 向/topic/marco发送消息
    @MessageMapping("/marco") 接收/topic/marco发送的消息，如果有返回值则默认返回路径/topic/marco
    @SendTo("/topic/marco") //@SendTo注解，消息将会发布到“/topic/marco”。所有订阅这个主题的应用（如客户端）都会收到这条消息。

    在控制器的@MessageMapping或@SubscribeMapping方法中，处理消息时有两种方式了解用户信息。在处理器方法中，
    通过简单地添加一个Principal参数，这个方法就能知道用户是谁并利用该信息关注此用户相关的数据。除此之外，
    处理器方法还可以使用@SendToUser注解，表明它的返回值要以消息的形式发送给某个认证用户的客户端（只发送给该客户端）。

    convertAndSend(destination, payload); //将消息广播到特定订阅路径中，类似@SendTo
    convertAndSendToUser(user, destination, payload);//将消息推送到固定的用户订阅路径中，类似@SendToUser

     */


    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public ServerResponse<String> handleException(CustomGenericException e) {
        // ...
        return ServerResponse.createByErrorMessage("发生错误: "+e.getErrMsg());
    }
 

 
}
