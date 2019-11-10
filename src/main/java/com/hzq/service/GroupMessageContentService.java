package com.hzq.service;

import com.hzq.vo.ServerResponse;
import com.hzq.domain.GroupMessageContent;
import com.hzq.vo.SendMessage;

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
     * @param message 群聊消息
     * @return 返回通用对象
     */
    ServerResponse insert(SendMessage message);

    /**
     * 查询群聊所有的消息
     * @param groupId 群的id
     * @param userId 用户id
     * @return 返回通用对象
     */
    ServerResponse<List<SendMessage>> selectAll(Integer groupId, Integer userId);

    /**
     * 删除群聊消息
     * @param id 消息id
     * @param groupId 群的id
     * @param userId 用户id
     * @return 返回通用对象
     */
    ServerResponse delete(Integer id, Integer groupId, Integer userId);

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
     * @return 返回群聊消息的通用对象
     */
    ServerResponse<List<SendMessage>> selectAllUnread(Integer userId);

    /**
     * 根据未读消息，更新未读记录
     * @param messageContents 未读消息
     * @param userId 用户userId
     * @return 返回通用对象
     */
    ServerResponse update(List<SendMessage> messageContents, Integer userId);
}
