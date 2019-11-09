package com.hzq.service;

import com.hzq.vo.ServerResponse;
import com.hzq.vo.SendMessage;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: 处理私聊
 * @version: 1.0
 */
public interface MessageService {

    /**
     *  插入私聊消息
     * @param message 消息
     * @param messageStatus 0 未读 1 已读
     * @param userId 该消息属于哪个用户
     * @return 返回通用对象
     */
    ServerResponse<String> insert(SendMessage message, Integer messageStatus, Integer userId);



    /**
     * 根据两个id更新多条消息状态
     * @param messageList  未读消息集合
     */
    void update(List<SendMessage> messageList);

    /**
     * 根据消息id删除消息
     * @param id 消息id
     * @return 返回通用对象
     */
    ServerResponse<String> deleteMessageById(Integer id);

    /**
     * 根据用户id和好友id删除消息
     * @param userId 用户id
     * @param friendId 好友id
     * @return 返回通用对象
     */
    ServerResponse<String>  deleteMessageByUserIdAndFriendId(Integer userId, Integer friendId);

    /**
     * 根据用户id和好友id查询聊天记录
     * @param id 用户id
     * @param friendId 好友id
     * @return 返回通用对象
     */
    ServerResponse<List<SendMessage>> queryMessageByUserIdAndFriendId(Integer id, Integer friendId);

    /**
     *
     * 根据用户id查询所有私聊未读消息
     * @param userId 用户主表id
     * @return 返回未读消息通用对象
     */
    ServerResponse<List<SendMessage>>  selectUnReadSendMessage(Integer userId);
}
