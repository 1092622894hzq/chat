package com.hzq.service;

import com.hzq.common.ServerResponse;
import com.hzq.domain.GroupMessageContent;

import java.util.List;
import java.util.Map;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: com.hzq.service
 * @version: 1.0
 */
public interface GroupMessageContentService {

    /**
     * 存储群聊消息
     * @param groupMessageContent 群聊消息
     * @return 返回通用对象
     */
    ServerResponse<String> insert(GroupMessageContent groupMessageContent);

    /**
     * 查询群聊所有的消息
     * @param groupId 群的id
     * @return 返回通用对象
     */
    ServerResponse<List<GroupMessageContent>> selectAll(Integer groupId);

    /**
     * 删除群聊消息
     * @param groupId 群的id
     * @return 返回通用对象
     */
    ServerResponse<String> delete(Integer groupId);

    /**
     * 获取未读的群聊消息
     * @param userId 用户id
     * @param groupId 群的id
     * @return 返回通用对象
     */
    ServerResponse<List<GroupMessageContent>> selectUnread(Integer userId,Integer groupId);

    /**
     * 获取所有群未读的群聊消息
     * @param userId 用户id
     * @return 返回群聊消息的map通用对象
     */
    ServerResponse<Map<Integer,List<GroupMessageContent>>> selectAllUnread(Integer userId);

}
