package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.domain.Message;
import com.hzq.domain.User;
import com.hzq.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: com.hzq.controller
 * @version: 1.0
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    //暂时认为toid由安卓安放
    public ServerResponse<String> insert(HttpSession session, Message message) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        message.setMessageFromId(user.getId());
        message.setMessageStatus(Const.MARK_AS_READ);
        message.setMessageType(Const.TEXT);
        message.setMessageGroup(Const.DEFAULT_GROUP);
        if (message.getMessageToId() != null && message.getMessageToId() > user.getId()) {
            message.setBigIdDelete(message.getMessageToId());
            message.setSmallIdDelete(user.getId());
        } else {
            message.setBigIdDelete(user.getId());
            message.setSmallIdDelete(message.getMessageToId());
        }
        return messageService.insert(message);
    }

    @RequestMapping("/delete")
    public ServerResponse<String> deleteMessageByUserId(HttpSession session, Message message) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        message.setId(user.getId());
        if (message.getMessageToId() != null && message.getMessageToId() > user.getId()) {
            message.setBigIdDelete(message.getMessageToId());
            message.setSmallIdDelete(user.getId());
        } else {
            message.setBigIdDelete(user.getId());
            message.setSmallIdDelete(message.getMessageToId());
        }
        return messageService.deleteMessageByUserId(message);
    }

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public ServerResponse<List<Message>> queryMessageByUserIdAndFriendId(@RequestParam("id") Integer id, @RequestParam("friendId") Integer friendId) {
        return messageService.queryMessageByUserIdAndFriendId(id,friendId);
    }

    @RequestMapping(value = "/queryUnreadMessage", method = RequestMethod.GET)
    public ServerResponse<List<Message>> queryUnreadMessageByUserId(HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return messageService.queryUnreadMessageByUserId(user.getId());
    }

}












