package com.hzq.dao;


import com.hzq.domain.GroupToUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/6
 * @Description: 对用户和群聊，群聊消息的关系进行操作
 * @version: 1.0
 */
public interface GroupToUserDao {

    /**
     * 添加用户进入群聊
     * @param groupToUser 用户在群聊中显示的信息
     * @return 返回修改次数
     */
    int insert(GroupToUser groupToUser);

    /**
     * 从群聊中删除用户
     * @param userId 用户id
     * @param groupId 群聊id
     * @return 返回修改次数
     */
    int delete(@Param("userId") Integer userId, @Param("groupId") Integer groupId);

    /**
     * 根据表的id删除用户
     * @param id 表的id
     * @return 返回修改次数
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 更新用户根据id在群聊中的信息
     * @param groupToUser 用户在群聊中的信息
     * @return 返回修改次数
     */
    int updateById(GroupToUser groupToUser);

    /**
     * 更新用户在群聊中的信息
     * @param groupToUser 用户在群聊中的信息
     * @return 返回修改次数
     */
    int update(GroupToUser groupToUser);

    /**
     * 根据群id和用户id查询
     * @param userId 用户id
     * @param groupId 群id
     * @return 返回用户在群内显示的信息
     */
    GroupToUser selectGroupToUser(@Param("userId") Integer userId, @Param("groupId") Integer groupId);

    /**
     *  根据群id查询所有群用户
     * @param groupId 群id
     * @return 返回所有群用户的集合
     */
    List<GroupToUser> selectByGroupId(@Param("groupId") Integer groupId);


}
