package com.hzq.dao;

import com.hzq.domain.UserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.dao
 * @version: 1.0
 */
public interface UserInfoDao {

    /**
     * 插入用户个人信息
     * @param userInfo 用户个人信息
     * @return 返回修改次数
     */
    int insert(UserInfo userInfo);

    /**
     * 修改用户个人信息
     * where的条件：用户主表的id
     * @param userInfo 修改的个人信息
     * @return 返回修改次数
     */
    int update(UserInfo userInfo);

    /**
     * 根据用户名查询用户个人信息
     * @param username 用户名
     * @return 返回用户个人信息
     */
    UserInfo queryUserByName(@Param("username") String username);

    /**
     * 根据用户id查询用户个人信息
     * @param userId 用户id
     * @return 返回用户个人信息
     */
    UserInfo queryUserById(@Param("userId") Integer userId);

    /**
     * 查询邮箱是否存在
     * @param email 邮箱
     * @return 返回查询到的条数
     */
    int checkEmail(@Param("email") String email);

    /**
     * 根据用户id获取用户邮箱
     * where的条件：主表id
     * @param userId 用户id
     * @return 返回查询到的邮箱
     */
    String selectEmailByUserId(@Param("userId") Integer userId);

}
