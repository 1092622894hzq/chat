package com.hzq.service.Impl;

import com.hzq.domain.Message;
import com.hzq.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @Auther: blue
 * @Date: 2019/10/8
 * @Description: com.hzq.service.Impl
 * @version: 1.0
 */
@Service("webSocketService")
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private SimpMessagingTemplate template; //可能会出现问题

    @Override
    public void sendChatMessage(Message message) {
        //可以看出template最大的灵活就是我们可以获取前端传来的参数来指定订阅地址
        //前面参数是订阅地址，后面参数是消息信息
        template.convertAndSend("/user/"+message.getMessageToId(), message.getMessageContent());
    }
}
