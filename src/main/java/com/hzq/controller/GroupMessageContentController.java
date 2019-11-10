package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.domain.User;
import com.hzq.vo.ServerResponse;
import com.hzq.domain.GroupMessageContent;
import com.hzq.service.GroupMessageContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: 群消息
 * @version: 1.0
 */
@RestController
@RequestMapping("/groupMessage")
public class GroupMessageContentController {

    @Autowired
    private GroupMessageContentService messageContentService;

    /**
     * 根据群聊id查讯所有群聊消息
     * @param id 群聊id
     * @return 通用对象
     */
    @RequestMapping(value = "/selectAll/{id}", method = RequestMethod.GET)
    public ServerResponse selectAll(@PathVariable Integer id, HttpSession session) {
        Integer userId = ((User)session.getAttribute(Const.CURRENT_USER)).getId();
        return messageContentService.selectAll(id,userId);
    }

    /**
     * 用户删除自己的群聊记录
     * @param id 消息id
     * @param  groupId 群id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/delete/{id}/{groupId}", method = RequestMethod.GET)
    public ServerResponse delete(@PathVariable Integer id, @PathVariable Integer groupId, HttpSession session) {
        Integer userId = ((User)session.getAttribute(Const.CURRENT_USER)).getId();
        return messageContentService.delete(id,groupId,userId);
    }

    /**
     * 根据用户id和群聊id查询未读消息
     * @param userId 用户id
     * @param groupId 群聊id
     * @return 通用对象
     */
    @RequestMapping(value = "/selectUnread/{userId}/{groupId}", method = RequestMethod.GET)
    public ServerResponse<List<GroupMessageContent>> selectUnread(@PathVariable Integer userId, @PathVariable Integer groupId) {
        return messageContentService.selectUnread(userId,groupId);
    }


}



