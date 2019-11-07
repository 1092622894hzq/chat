package com.hzq.controller;

import com.hzq.vo.ServerResponse;
import com.hzq.domain.GroupMessageContent;
import com.hzq.service.GroupMessageContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ServerResponse<List<GroupMessageContent>> selectAll(@PathVariable Integer id) {
        return messageContentService.selectAll(id);
    }

    /**
     * 用户删除自己的群聊记录
     * @param id 用户id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ServerResponse<String> delete(@PathVariable Integer id) {
        return messageContentService.delete(id);
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



