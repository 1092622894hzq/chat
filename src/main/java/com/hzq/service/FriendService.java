package com.hzq.service;

import com.hzq.common.ServerResponse;
import com.hzq.domain.Friend;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.service
 * @version: 1.0
 */
public interface FriendService {

    /**
     * 添加好友
     * @param friend 好有信息
     * @return 返回通用对象
     */
    ServerResponse<String> insert(Friend friend);

    /**
     * 更新好友  用户id和好友id不可缺
     * @param friend 好友信息
     * @return 返回通用对象
     */
    ServerResponse<String> update(Friend friend);

    /**
     * 删除好友
     * @param userId 用户id
     * @param friendId 好友昵称
     * @return 返回通用对象
     */
    ServerResponse<String> delete(Integer userId, Integer friendId);

    /**
     * 根据用户id查询所有用户好友
     * @param userId 用户id
     * @return 返回通用对象
     */
    ServerResponse<List<Friend>> selectAll(Integer userId);

    /**
     * 根据用户id和好友昵称查询好友
     * @param userId 用户id
     * @param friendName 好于昵称
     * @return 返回包含好友的通用对象
     */
    ServerResponse<Friend> selectFriendByFriendName(Integer userId, String friendName);

}
