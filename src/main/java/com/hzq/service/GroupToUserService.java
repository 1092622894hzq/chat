package com.hzq.service;

import com.hzq.vo.GroupUserVo;
import com.hzq.vo.ServerResponse;
import com.hzq.domain.GroupToUser;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/6
 * @Description: 对用户和群聊，群聊消息的关系进行操作
 * @version: 1.0
 */
public interface GroupToUserService {

    /**
     * 将用户加入群聊中
     * @param userId  用户id
     * @param groupId 群id
     * @return 返回通用对象
     */
    ServerResponse insert(Integer userId, Integer groupId);

    /**
     * 将用户从群聊中删除
     * @param userId 用户id
     * @param groupId  群聊id
     * @return 返回通用对象
     */
    ServerResponse<String> delete(Integer userId, Integer groupId);

    /**
     * 更新用户在群聊中显示的信息 根据用户id和群id
     * @param groupToUser 用户的在群聊显示的信息
     */
    void update(GroupToUser groupToUser);

    /**
     * 根据群id和用户id查找用户在群信息
     * @param userId 用户id
     * @param groupId 群id
     * @return 返回包含用户在群信息的通用对象
     */
    ServerResponse<GroupUserVo> selectGroupToUser(Integer userId, Integer groupId);

    /**
     * 根据群id找出所有群用户
     * @param groupId 群id
     * @return 返回所有群用户的集合
     */
    ServerResponse<List<GroupToUser>> selectByGroupId(Integer groupId);

    /**
     * 根据群id找出所有群用户和个人信息
     * @param groupId 群id
     * @return 返回所有群用户的集合
     */
    ServerResponse<List<GroupUserVo>> select(Integer groupId);


}
