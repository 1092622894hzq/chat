package com.hzq.dao;

import com.hzq.vo.Result;
import com.hzq.domain.User;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.dao
 * @version: 1.0
 */
public interface UserDao {

    /**
     * 插入用户
     * @param user 用户
     * @return 返回修改次数
     */
    int insert(User user);

    /**
     * 根据名字查询用户
     * @param username 用户名
     * @return 返回用户
     */
    User selectLogin(@Param("username") String username);

    /**
     * 根据用户id删除用户
     * @param id 用户id
     * @return 返回修改次数
     */
    int deleteUserByPrimaryId(@Param("id") Integer id);

    /**
     * 修改密码
     * @param newPassword 新密码
     * @param id 用户id
     * @return 返回修改次数
     */
    int updatePassword(@Param(("newPassword")) String newPassword, @Param("id") Integer id);

    /**
     * 更新用户状态
     * @param status 状态
     * @param id 用户id
     * @return 返回更改次数
     */
    int updateStatus(@Param("status") Integer status, @Param("id") Integer id);

    /**
     * 根据用户名查询用户是否存在
     * @param username 用户名
     * @return 返回查询到的数据条数
     */
    int checkUsername(@Param("username") String username);

    /**
     * 根据用户id查询用户是否存在
     * @param userId 用户id
     * @return 返回查询到的数据条数
     */
    int checkUserId(@Param("userId") Integer userId);

    /**
     * 根据用户id查询用户
     * @param id 用户id
     * @return 返回用户对象
     */
    User selectUserById(@Param("id") Integer id);



    Result selectAll(User user);
















}
