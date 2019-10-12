package com.hzq.dao;

import com.hzq.domain.Group;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: com.hzq.dao
 * @version: 1.0
 */
public interface GroupDao {

    /**
     * 创建群聊
     * @param group 群信息
     * @return 返回修改次数
     */
    int insert(Group group);

    /**
     * 根据群id删除群聊
     * @param id 群id
     * @return 返回修改次数
     */
    int delete(@Param("id") Integer id);

    /**
     * 根据群id修改群信息
     * @param group 修改的信息
     * @return 返回修改次数
     */
    int update(Group group);

    /**
     * 根据用户id查询所有群
     * @param userId 用户id
     * @return 返回所有群的集合
     */
    List<Group> selectAll(@Param("userId") Integer userId);

    /**
     * 根据群id查询群信息
     * @param id 群id
     * @return 返回群信息
     */
    Group selectGroup(@Param("id") Integer id);


}
