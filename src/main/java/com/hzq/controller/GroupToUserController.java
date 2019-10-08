package com.hzq.controller;

import com.hzq.common.ServerResponse;
import com.hzq.domain.GroupToUser;
import com.hzq.service.GroupToUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: blue
 * @Date: 2019/10/6
 * @Description: com.hzq.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("/groupToUser")
public class GroupToUserController {

    @Autowired
    private GroupToUserService groupToUserService;

    /**
     * 添加用户进入群聊
     * @param groupToUser 用户在群聊中显示的相关信息
     * @return 返回通用结果
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> insert(@RequestBody GroupToUser groupToUser) {
        return groupToUserService.insert(groupToUser);
    }

    /**
     * 根据用户id和群id将用户从群聊移除
     * @param groupUserId 用户id
     * @param groupId 群id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/delete/{groupUserId}/{groupId}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> delete(@PathVariable Integer groupUserId, @PathVariable Integer groupId) {
        return groupToUserService.delete(groupUserId,groupId);
    }

    /**
     * 根据id将用户从群聊中移除
     * @param id 表的id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> deleteById(@PathVariable Integer id){
        return groupToUserService.deleteById(id);
    }

    /**
     * 根据该表的id来进行更新信息
     * @param groupToUser 要更新的信息，该表的id不能为空
     * @return 返回通用对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> update(@RequestBody GroupToUser groupToUser) {
        return groupToUserService.update(groupToUser);
    }

    /**
     * 根据用户id和群id查询用户
     * @param groupUserId 用户id
     * @param groupId 群id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/select/{groupUserId}/{groupId}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<GroupToUser> selectGroupToUser(@PathVariable Integer groupUserId, @PathVariable Integer groupId) {
        return groupToUserService.selectGroupToUser(groupUserId,groupId);
    }
}
