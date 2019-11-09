package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.vo.ServerResponse;
import com.hzq.domain.Group;
import com.hzq.domain.User;
import com.hzq.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: 群
 * @version: 1.0
 */
@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    /**
     * 创建群聊
     *  必给：,groupAdminId
     * @param group 群信息
     * @return 返回通用对象
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ServerResponse<Group> insert(@RequestBody Group group) {
        groupService.insert(group);
        return groupService.select(group.getId());
    }

    /**
     * 更新群聊信息
     * @param group 要更新的群聊消息，必须包含群id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ServerResponse<Group> update(@RequestBody Group group) {
        groupService.update(group);
        return groupService.select(group.getId());
    }


    /**
     * 删除群聊
     * @param id 群聊id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ServerResponse<String> delete(@PathVariable Integer id) {
        return groupService.delete(id);
    }

    /**
     * 选出所有群聊
     * @param session 一次会话
     * @return 返回存有所有群聊的通用对象
     */
    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    public ServerResponse<List<Group>> selectAll(HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return groupService.selectAll(user.getId());
    }

}
