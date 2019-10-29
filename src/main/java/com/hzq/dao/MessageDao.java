package com.hzq.dao;

import com.hzq.domain.Message;
import com.hzq.vo.SendMessage;
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
     * @param msg 消息
     * @param messageStatus 0 未读，1 已读
     * @param userId 该消息属于哪个用户
     * @return 返回修改次数
     */
    int insert(@Param("msg") SendMessage msg, @Param("messageStatus") Integer messageStatus, @Param("userId") Integer userId);

    /**
     * 根据用户id和好友id查询聊天记录
     * @param userId  用户主表id
     * @param friendId 好友主表id
     * @return 返回聊天记录集合
     */
    List<Message> queryMessageByUserIdAndFriendId(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

    /**
     * 更新一条消息状态
     * @param id 消息id
     * @param messageStatus 已读
     */
    int updateOneMessage(@Param("id") Integer id, @Param("messageStatus") Integer messageStatus);

    /**
     * 更新两个id之间的消息状态
     * @param bigId 大的id
     * @param smallId 消息的id
     * @param messageStatus 标记已读
     * @param userId 用户的id
     */
    int update(@Param("bigId") Integer bigId, @Param("smallId") Integer smallId,  @Param("messageStatus") Integer messageStatus, @Param("userId") Integer userId);

    /**
     * 根据消息删除消息
     * @param id 消息id
     */
    void deleteMessageById(@Param("id") Integer id);

    /**
     * 根据用户id和好友id删除消息
     * @param userId 用户id
     * @param friendId 好友id
     */
    void deleteMessageByUserIdAndFriendId(@Param("userId") Integer userId, @Param("friendId") Integer friendId);


    /**
     *  f.friend_id != f.user_id 避免把自己也查询进去
     * @param userId 用户id
     * @param messageStatus 消息状态 0 未读 1 已读
     * @return 返回消息的集合
     */
    List<SendMessage> selectUnReadSendMessage(@Param("userId") Integer userId, @Param("messageStatus") Integer messageStatus);
}
