package com.hzq.dao;

import com.hzq.domain.GroupMessageContent;
import com.hzq.vo.SendMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: 群聊
 * @version: 1.0
 */
public interface GroupMessageContentDao {

    /**
     * 存储群聊消息
     * @param msg 群聊消息
     * @return 返回修改的次数
     */
    int insert(@Param("msg") SendMessage msg);

    /**
     * 添加删除消息的id
     * @param groupId 群id
     * @param userId 用户id
     * @param groupMessageId 消息id
     * @return 修改次数
     */
    int insertToDelete(@Param("groupId") Integer groupId, @Param("userId") Integer userId, @Param("groupMessageId") Integer groupMessageId);

    /**
     * 查询群聊所有的消息
     * @param groupId 群的id
     * @param userId 用户id
     * @return 返回群聊消息的集合
     */
    List<SendMessage> selectAll(@Param("groupId") Integer groupId, @Param("userId") Integer userId);

    /**
     * 获取指定群未读的群聊消息
     * @param userId 用户id
     * @param groupId 群的id
     * @return 返回群聊消息的集合
     */
    List<GroupMessageContent> selectUnread(@Param("userId") Integer userId, @Param("groupId") Integer groupId);

    /**
     * 获取所有群未读的群聊消息
     * @param userId 用户id
     * @return 返回群聊消息集合
     */
    List<SendMessage> selectAllUnread(@Param("userId") Integer userId);

}
