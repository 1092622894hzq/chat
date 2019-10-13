package com.hzq.controller;

import com.google.gson.Gson;
import com.hzq.execption.CustomGenericException;
import com.hzq.common.ServerResponse;
import com.hzq.domain.Content;
import com.hzq.domain.GroupMessageContent;
import com.hzq.domain.Message;
import com.hzq.service.GroupMessageContentService;
import com.hzq.service.MessageService;
import com.hzq.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Map;
 

@RestController
public class ChatController {

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private GroupMessageContentService groupMessageContentService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private WebSocketService webSocketService;
    //日志打印
    private static Logger LOGGER = LoggerFactory.getLogger(ChatController.class);
    //json转换
    private static Gson gson = new Gson();

    private <T>String toJson(T obj){
        return gson.toJson(obj);
    }


    //处理群消息
    @MessageMapping("/group")
    public void sendTogroup(@Payload GroupMessageContent content) {
        Integer groupId = content.getGroupId();
        ServerResponse response = groupMessageContentService.insert(content);
        if (!response.isSuccess()) {
            LOGGER.debug(response.getMsg());
        }
        template.convertAndSend("/topic/group/"+groupId,toJson(content));
    }

    //处理私聊消息
    @MessageMapping("/friend")
    public void sendToOther(@Payload Message message) {
        LOGGER.debug(message.getMessageContent());
        Integer toId = message.getMessageToId();
        ServerResponse response = messageService.insert(message);
        if (!response.isSuccess()) {
            LOGGER.debug(response.getMsg());
        }
        template.convertAndSendToUser(toId.toString(),"queue/result",toJson(message));
    }

    //处理异常消息
    @MessageExceptionHandler
    @SendToUser("/errors")
    public ServerResponse<String> handleException(CustomGenericException e) {
        return ServerResponse.createByErrorMessage("发生错误: "+e.getErrMsg());
    }


    //    /**
//     * 表示服务端可以接收客户端通过主题“/app/hello”发送过来的消息，客户端需要在主题"/topic/hello"上监听并接收服务端发回的消息
//     * @param topic 内容主题   greetings
//     * @param headers  {simpMessageType=MESSAGE, stompCommand=SEND, nativeHeaders={destination=[/app/hello], content-length=[18], atytopic=[greetings]}, DestinationVariableMethodArgumentResolver.templateVariables={}, simpSessionAttributes={}, lookupDestination=/hello, simpSessionId=deovbx2n, simpDestination=/app/hello, id=cc6b83a1-54a9-b025-d6c1-9c49e81951bd, timestamp=1570536487294}
//     */
//    @MessageMapping("/hello")
//   // @SendTo("/topic/greetings")
//    public void greeting(Content msg, @Header("atytopic") String topic, @Headers Map<String, Object> headers) {
//        System.out.println("connected successfully....");
//        System.out.println("Content: "+msg.getMessage());
//        System.out.println(topic);
//        System.out.println(headers);
//        Timestamp time = new Timestamp((Long) headers.get("timestamp"));
//        System.out.println("time:"+time+"---"+headers.get("timestamp"));
//    }

//
//    @SendToUser("/queue/position-updates")
//    public Message executeTrade(Message trade, Principal principal) {
//        principal.getName();
//        // ...
//        return trade;
//    }
//
//    /*
//    在一对一向服务器订阅的时候,保存并注册用户信息,
//    @DestinationVariable String userId,这个注解类似于@Controller的PathVarable
//    可以获取订阅方的用户ID,方便实时回复个人订阅状态信息
//     */
//
//    /**
//     * 服务端接收一对多响应
//     */
//    @MessageMapping("/sendTopic")
//    @SendTo("/topic/getResponse")
//    public ServerMessage sendTopic(ClientMessage message, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
//        LOGGER.info("接收到了信息" + message.getName());
//        return new ServerMessage("一对多服务 响应");
//    }
//    /**
//     * 服务端接收一对一响应
//     */
//    @MessageMapping("/sendUser")
//    @SendToUser("/queue/getResponse")
//    public ServerMessage sendUser(ClientMessage message, StompHeaderAccessor stompHeaderAccesso,Principal principal) {
//        stompHeaderAccesso.getSessionAttributes();
//        LOGGER.info("接收到了信息" + message.getName());
//        return new ServerMessage("一对一服务 响应");
//    }
//    /**
//     * 一对一订阅通知
//     */
//    @SubscribeMapping("user/{userId}/queue/getResponse")
//    public ServerMessage subOnUser(@DestinationVariable String userId, StompHeaderAccessor stompHeaderAccessor,Principal principal) {
//        stompHeaderAccessor.setUser(new Principal() {
//            @Override
//            public String getName() {
//                return userId;
//            }
//        });
//        LOGGER.info(userId + "/queue/getResponse 已订阅");
//        return new ServerMessage("感谢你订阅了 一对一服务");
//    }
//    /**
//     * 一对多订阅通知
//     */
//    @SubscribeMapping("/topic/getResponse")
//    public ServerMessage subOnTopic(Principal principal) {
//        LOGGER.info("/topic/getResponse 已订阅");
//        return new ServerMessage("感谢你订阅了 一对多服务");
//    }
//
//    /**
//     * 这里用的是@SendToUser，这就是发送给单一客户端的标志。本例中，
//     * 客户端接收一对一消息的主题应该是“/user/” + 用户Id + “/message” ,这里的用户id可以是一个普通的字符串，只要每个用户端都使用自己的id并且服务端知道每个用户的id就行。
//     * @return 返回通用对象
//     */
//    @MessageMapping("/message")
//  //  @SendToUser("/{userId}/message")
//    public String handleSubscribe(Content msg) {
//        System.out.println("this is the @SubscribeMapping('/marco')");
//     //   simpMessageSendingOperations.convertAndSendToUser();
//        return msg.toJson();
//
//    }
//    /*
//
//    使用Spring和STOMP消息功能的时候，我们有三种方式利用认证用户：
//    @MessageMapping和@SubscribeMapping标注的方法能够使用Principal来获取认证用户；
//    @MessageMapping、@SubscribeMapping和@MessageException方法返回的值能够以消息的形式发送给认证用户；
//    SimpMessagingTemplate能够发送消息给特定用户。
//    @SubscribeMapping("/marco") 向/topic/marco发送消息
//    @MessageMapping("/marco") 接收/topic/marco发送的消息，如果有返回值则默认返回路径/topic/marco
//    @SendTo("/topic/marco") //@SendTo注解，消息将会发布到“/topic/marco”。所有订阅这个主题的应用（如客户端）都会收到这条消息。
//
//    在控制器的@MessageMapping或@SubscribeMapping方法中，处理消息时有两种方式了解用户信息。在处理器方法中，
//    通过简单地添加一个Principal参数，这个方法就能知道用户是谁并利用该信息关注此用户相关的数据。除此之外，
//    处理器方法还可以使用@SendToUser注解，表明它的返回值要以消息的形式发送给某个认证用户的客户端（只发送给该客户端）。
//
//    convertAndSend(destination, payload); //将消息广播到特定订阅路径中，类似@SendTo
//    convertAndSendToUser(user, destination, payload);//将消息推送到固定的用户订阅路径中，类似@SendToUser
//
//     */
//
//
//    @MessageExceptionHandler
//  //  @SendToUser("/queue/errors")
//    public ServerResponse<String> handleException(CustomGenericException e) {
//        // ...
//        return ServerResponse.createByErrorMessage("发生错误: "+e.getErrMsg());
//    }
//

 
}
