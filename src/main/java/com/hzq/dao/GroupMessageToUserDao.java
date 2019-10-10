package com.hzq.dao;

import com.hzq.domain.GroupMessageToUser;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/10/4
 * @Description: com.hzq.dao
 * @version: 1.0
 */
public interface GroupMessageToUserDao {

    /**
     * 添加用户和群聊消息的关系
     * @param messageToUser 用户和群聊绑定信息
     * @return 返回修改次数
     */
    int insert(GroupMessageToUser messageToUser);

    /**
     * 根据用户id更新用户和群聊消息的关系
     * @param userId 用户id
     * @param groupMessageId 群消息id
     * @param time 已读消息的时间
     * @return 返回修改次数
     */
    int update(@Param("userId") Integer userId, @Param("groupMessageId") Integer groupMessageId, @Param("time") Timestamp time);

    /**
     * 根据用户id删除所有群聊消息消息
     * @param userId 用户id
     * @return 返回修改次数
     */
    int delete(@Param("userId") Integer userId);


}
