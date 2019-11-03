package com.hzq.service;

import com.hzq.vo.ServerResponse;
import com.hzq.domain.Friend;
import com.hzq.vo.FriendVo;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 好友
 * @version: 1.0
 */
public interface FriendService {

    /**
     * 更新好友  用户id和好友id不可缺
     * @param friend 好友信息
     * @return 返回通用对象
     */
    ServerResponse<String> update(Friend friend);

    /**
     * 删除好友
     * @param userId 用户id
     * @param friendId 好友id
     * @return 返回通用对象
     */
    ServerResponse<String> delete(Integer userId, Integer friendId);

    /**
     * 根据用户id和准好友id来查询好友信息，并完成好友的插入
     * @param userId 用户id
     * @param friendId 准好友id
     * @return 返回结果
     */
    ServerResponse<String> insertFriendBySelect(Integer userId, Integer friendId);

    /**
     * 根据用户id查询所有用户好友
     * @param userId 用户id
     * @return 返回通用对象
     */
    ServerResponse<List<FriendVo>> selectAll(Integer userId);

    /**
     * 根据用户id和好友昵称查询好友
     * @param userId 用户id
     * @param friendName 好于昵称
     * @return 返回包含好友的通用对象
     */
    ServerResponse<FriendVo> selectFriendByFriendName(Integer userId, String friendName);

    /**
     * 根据用户id和好友id查询好友
     * @param userId 用户id
     * @param friendId 好友id
     * @return 返回对象信息
     */
    ServerResponse<FriendVo> selectFriendByFriendId(Integer userId, Integer friendId);

}
