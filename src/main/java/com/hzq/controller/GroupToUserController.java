package com.hzq.controller;

import com.hzq.vo.GroupUserVo;
import com.hzq.vo.ServerResponse;
import com.hzq.domain.GroupToUser;
import com.hzq.service.GroupToUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/6
 * @Description: 群用户
 * @version: 1.0
 */
@RestController
@RequestMapping("/groupToUser")
public class GroupToUserController {

    @Autowired
    private GroupToUserService groupToUserService;

    /**
     * 添加用户进入群聊
     * 必须参数 userId, groupId
     * @param userId 用户id
     * @param groupId 群id
     * @return 返回通用结果
     */
    @RequestMapping(value = "/insert/{userId}/{groupId}", method = RequestMethod.GET)
    public ServerResponse<List<GroupUserVo>> insert(@PathVariable Integer userId, @PathVariable Integer groupId) {
         groupToUserService.insert(userId,groupId);
         return groupToUserService.select(groupId);
    }

    /**
     * 根据用户id和群id将用户从群聊移除
     * @param userId 用户id
     * @param groupId 群id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/delete/{userId}/{groupId}", method = RequestMethod.GET)
    public ServerResponse<String> delete(@PathVariable Integer userId, @PathVariable Integer groupId) {
        return groupToUserService.delete(userId,groupId);
    }

    /**
     * 根据该表的id来进行更新信息
     * @param groupToUser 要更新的信息，
     * @return 返回通用对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ServerResponse<GroupUserVo> update(@RequestBody GroupToUser groupToUser) {
        groupToUserService.update(groupToUser);
        return groupToUserService.selectGroupToUser(groupToUser.getUserId(),groupToUser.getGroupId());
    }

    /**
     * 根据用户id和群id查询用户
     * @param userId 用户id
     * @param groupId 群id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/select/{userId}/{groupId}", method = RequestMethod.GET)
    public ServerResponse<GroupUserVo> selectGroupToUser(@PathVariable Integer userId, @PathVariable Integer groupId) {
        return groupToUserService.selectGroupToUser(userId,groupId);
    }

    /**
     * 根据群id查询用户信息
     * @param groupId 群id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/select/{groupId}", method = RequestMethod.GET)
    public ServerResponse<List<GroupUserVo>> select(@PathVariable Integer groupId) {
        return groupToUserService.select(groupId);
    }
}
