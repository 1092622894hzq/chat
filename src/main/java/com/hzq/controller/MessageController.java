package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.domain.Message;
import com.hzq.domain.User;
import com.hzq.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

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
    public ServerResponse<String> insert(HttpSession session, Message message) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        message.setMessageFromId(user.getId());
        message.setMessageStatus(Const.MARK_AS_READ);
        message.setMessageType(Const.TEXT);
        message.setUserId(user.getId());
        return messageService.insert(message);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ServerResponse<String> deleteMessageById(@PathVariable("id") Integer id) {
        return messageService.deleteMessageById(id);
    }

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public ServerResponse<List<Message>> queryMessageByUserIdAndFriendId(@RequestParam("id") Integer id, @RequestParam("friendId") Integer friendId) {
        return messageService.queryMessageByUserIdAndFriendId(id,friendId);
    }

    @RequestMapping(value = "/queryUnreadMessage", method = RequestMethod.GET)
    public ServerResponse<Map<Integer,List<Message>>> queryUnreadMessageByUserId(HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return messageService.queryUnreadMessageByUserId(user.getId());
    }

}












