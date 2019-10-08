package com.hzq.dao;


import com.hzq.domain.GroupToUser;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: blue
 * @Date: 2019/10/6
 * @Description: com.hzq.dao
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
     * @param groupUserId 群聊id
     * @param groupId 用户id
     * @return 返回修改次数
     */
    int delete(@Param("groupUserId") Integer groupUserId, @Param("groupId") Integer groupId);

    /**
     * 根据表的id删除用户
     * @param id 表的id
     * @return 返回修改次数
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 更新用户在群聊中的信息
     * @param groupToUser 用户在群聊中的信息
     * @return 返回修改次数
     */
    int update(GroupToUser groupToUser);

    /**
     * 根据群id和用户id查询
     * @param groupUserId 用户id
     * @param groupId 群id
     * @return 返回用户在群内显示的信息
     */
    GroupToUser selectGroupToUser(@Param("groupUserId") Integer groupUserId, @Param("groupId") Integer groupId);

}
