package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.domain.GroupMessageContent;
import com.hzq.service.GroupMessageContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: com.hzq.controller
 * @version: 1.0
 */
@RestController
@RequestMapping("/groupMessage")
public class GroupMessageContentController {

    @Autowired
    private GroupMessageContentService messageContentService;

    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    public ServerResponse<List<GroupMessageContent>> selectAll(@RequestParam("id") Integer id) {
        return messageContentService.selectAll(id);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ServerResponse<String> delete(@RequestParam("id") Integer id) {
        return messageContentService.delete(id);
    }

    @RequestMapping(value = "/selectUnread", method = RequestMethod.GET)
    public ServerResponse<List<GroupMessageContent>> selectUnread(@RequestParam("userId") Integer userId, @RequestParam("groupId") Integer groupId) {
        return messageContentService.selectUnread(userId,groupId);
    }


}



