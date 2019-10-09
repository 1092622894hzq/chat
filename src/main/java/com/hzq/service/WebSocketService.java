package com.hzq.service;

import com.hzq.domain.Message;

/**
 * @Auther: blue
 * @Date: 2019/10/8
 * @Description: 定义简单的发送消息模板
 * @version: 1.0
 */
public interface WebSocketService {

    /**
     * 定义私发消息的方法
     * @param message 消息
     */
    void sendChatMessage(Message message);

}
