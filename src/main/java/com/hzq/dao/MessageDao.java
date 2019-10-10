package com.hzq.dao;

import com.hzq.domain.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: 私聊的消息
 * @version: 1.0
 */
public interface MessageDao {

    /**
     * 插入聊天消息
     * @param message 消息
     * @return 返回修改次数
     */
    int insert(@Param("message") Message message);

    /**
     * 根据消息删除消息
     * @param id 消息id
     * @return 返回修改次数
     */
    int deleteMessageById(@Param("id") Integer id);

    /**
     * 根据用户id删除所有聊天信息
     * @param userId 用id
     * @return 返回修改次数
     */
    int deleteAllByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户id和好友id删除消息
     * @param userId 用户id
     * @param friendId 好友id
     * @return 返回修改次数
     */
    int deleteMessageByUserIdAndFriendId(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

    /**
     * 根据用户id和好友id查询聊天记录
     * @param userId  用户主表id
     * @param friendId 好友主表id
     * @return 返回聊天记录集合
     */
    List<Message> queryMessageByUserIdAndFriendId(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

    /**
     * 根据用户id查询所有私聊未读消息
     * @param userId 用户主表id
     * @param messageStatus 消息的状态
     * @return 返回未读消息集合
     */
    List<Message> queryUnreadMessageByUserId(@Param("userId") Integer userId, @Param("messageStatus") Integer messageStatus);

    /**
     * 更新一条消息状态
     * @param id 消息id
     * @param read 已读
     */
    void updateOneMessage(@Param("id") Integer id, @Param("read") Integer read);

    /**
     * 更新两个id之间的消息状态
     * @param bigId 大的id
     * @param smallId 消息的id
     * @param read 标记已读
     * @param userId 用户的id
     */
    void update(@Param("bigId") Integer bigId, @Param("smallId") Integer smallId,  @Param("read") Integer read, @Param("userId") Integer userId);

}
