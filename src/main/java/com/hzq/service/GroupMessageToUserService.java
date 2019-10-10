package com.hzq.service;

import com.hzq.common.ServerResponse;
import com.hzq.domain.GroupMessageToUser;

import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/10/4
 * @Description: com.hzq.service
 * @version: 1.0
 */
public interface GroupMessageToUserService {

    /**
     * 添加用户和消息的关系
     * @param messageToUser 用户和群消息的关系
     * @return 返回通用结果
     */
    ServerResponse<String> insert(GroupMessageToUser messageToUser);

    /**
     * 根据群消息id和用户id更新消息
     * @param groupMessageId 群消息的id
     * @param userId 用户id
     * @param time 当前时间
     * @return 返回通用结果
     */
    ServerResponse<String> update(Integer userId, Integer groupMessageId, Timestamp time);

    /**
     * 根据用户id删除所有群聊消息
     * @param userId 用户id
     * @return 返回通用对象
     */
    ServerResponse<String> delete(Integer userId);

    /**
     * 根据群消息id和用户id删除群内所有消息
     * @param groupMessageId 群消息的id
     * @param userId 用户id
     * @param time 当前时间
     * @return 返回通用结果
     */
    ServerResponse<String> deleteByGroupMessageAndUserId(Integer groupMessageId, Integer userId, Timestamp time);


}
