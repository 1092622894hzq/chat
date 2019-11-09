package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.vo.ServerResponse;
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

    /**
     * 根据消息id删除消息
     * @param id 消息id
     * @return 返回通用结果
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ServerResponse<String> deleteMessageById(@PathVariable Integer id) {
        return messageService.deleteMessageById(id);
    }

    /**
     * 根据用户id和好友id删除聊天记录
     * @param userId 用户id
     * @param friendId 好友id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/delete/{userId}/{friendId}", method = RequestMethod.GET)
    public ServerResponse<String> deleteMessageByUserIdAndFriendId(@PathVariable Integer userId, @PathVariable Integer friendId) {
        return messageService.deleteMessageByUserIdAndFriendId(userId,friendId);
    }

    /**
     * 根据用户id和好友id来查询聊天记录
     * @param id 用户id
     * @param friendId 好友id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/query/{id}/{friendId}", method = RequestMethod.GET)
    public ServerResponse<List<SendMessage>> queryMessageByUserIdAndFriendId(@PathVariable Integer id, @PathVariable Integer friendId) {
        return messageService.queryMessageByUserIdAndFriendId(id,friendId);
    }

    /**
     * 查询未读消息
     * @param session 一次会话
     * @return  返回未读消息
     */
    @RequestMapping(value = "/selectUnReadSendMessage", method = RequestMethod.GET)
    public ServerResponse selectUnReadSendMessage(HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return messageService.selectUnReadSendMessage(user.getId());
    }

}












