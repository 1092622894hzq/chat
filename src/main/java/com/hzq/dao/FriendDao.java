package com.hzq.dao;

import com.hzq.domain.Friend;
import com.hzq.vo.FriendVo;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLTransactionRollbackException;
import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.dao
 * @version: 1.0
 */
public interface FriendDao {

    /**
     * 添加好友
     * @param friend 好友
     * @return 返回修改次数
     */
    int insert(Friend friend);

    /**
     * 更新好友信息
     * @param friend 要更改的好友信息
     * @return 返回修改次数
     */
    int update(Friend friend);

    /**
     * 根据用户id和好友昵称删除好友
     * @param userId 用户主表id
     * @param friendId 好友id
     * @return 返回修改次数
     */
    int delete(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

    /**
     * 根据用户id删除所有好友
     * @param userId 用户id
     * @return 返回修改次数
     */
    int deleteById(@Param("userId") Integer userId);

    /**
     * 查询所有好友
     * @param userId 用户主表id
     * @return 返回好友集合
     */
    List<FriendVo> selectAll(@Param("userId") Integer userId);

    /**
     * 根据用户id和好友昵称查询好友
     * @param userId  用户主表id
     * @param friendName 好友昵称
     * @return 返回好友
     */
    FriendVo selectFriendByFriendName(@Param("userId") Integer userId, @Param("friendName") String friendName);

    /**
     * 根据用户id和好友id查询好友
     * @param userId 用户id
     * @param friendId 好友id
     * @return 返回好友
     */
    FriendVo selectFriendByFriendId(@Param("userId") Integer userId, @Param("friendId") Integer friendId);
}
