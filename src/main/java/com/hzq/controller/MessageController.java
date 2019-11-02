package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.domain.Message;
import com.hzq.domain.User;
import com.hzq.service.MessageService;
import com.hzq.vo.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: 私聊
 * @version: 1.0
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ServerResponse<String> deleteMessageById(@PathVariable Integer id) {
        return messageService.deleteMessageById(id);
    }

    @RequestMapping(value = "/delete/{userId}/{friendId}", method = RequestMethod.GET)
    public ServerResponse<String> deleteMessageByUserIdAndFriendId(@PathVariable Integer userId, @PathVariable Integer friendId) {
        return messageService.deleteMessageByUserIdAndFriendId(userId,friendId);
    }

    @RequestMapping(value = "/query/{id}/{friendId}", method = RequestMethod.GET)
    public ServerResponse<List<SendMessage>> queryMessageByUserIdAndFriendId(@PathVariable Integer id, @PathVariable Integer friendId) {
        return messageService.queryMessageByUserIdAndFriendId(id,friendId);
    }

    @RequestMapping(value = "/selectUnReadSendMessage", method = RequestMethod.GET)
    public ServerResponse<List<SendMessage>> selectUnReadSendMessage(HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        return messageService.selectUnReadSendMessage(user.getId());
    }

}












