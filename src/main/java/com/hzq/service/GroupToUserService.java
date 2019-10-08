package com.hzq.service;

import com.hzq.common.ServerResponse;
import com.hzq.domain.GroupToUser;

/**
 * @Auther: blue
 * @Date: 2019/10/6
 * @Description: com.hzq.service
 * @version: 1.0
 */
public interface GroupToUserService {

    /**
     * 将用户加入群聊中
     * @param groupToUser 用户在群聊中显示的信息
     * @return 返回通用对象
     */
    ServerResponse<String> insert(GroupToUser groupToUser);

    /**
     * 将用户从群聊中删除
     * @param groupUserId 用户id
     * @param groupId  群聊id
     * @return 返回通用对象
     */
    ServerResponse<String> delete(Integer groupUserId, Integer groupId);

    /**
     * 根据表的id删除用户
     * @param id 表的id
     * @return 返回通用对象
     */
    ServerResponse<String> deleteById(Integer id );

    /**
     * 更新用户在群聊中显示的信息
     * @param groupToUser 用户的在群聊显示的信息
     * @return 返回通用对象
     */
    ServerResponse<String> update(GroupToUser groupToUser);

    /**
     * 根据群id和用户id查找用户在群信息
     * @param groupUserId 用户id
     * @param groupId 群id
     * @return 返回包含用户在群信息的通用对象
     */
    ServerResponse<GroupToUser> selectGroupToUser(Integer groupUserId, Integer groupId);

}
