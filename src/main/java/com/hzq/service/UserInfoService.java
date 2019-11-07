package com.hzq.service;

import com.hzq.vo.ServerResponse;
import com.hzq.domain.UserInfo;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.service
 * @version: 1.0
 */
public interface UserInfoService {

    /**
     * 插入用户个人信息
     * @param userInfo 用户个人信息
     * @return 返回通用对象
     */
    ServerResponse<String> insert(UserInfo userInfo);

    /**
     * 根据用户id更新用户个人信息
     * where的条件：主表id
     * @param userInfo 要更新的个人信息
     * @return 返回通用对象
     */
    ServerResponse<String> update(UserInfo userInfo);

    /**
     * 根据用户查询用户个人信息
     * @param username 用户名
     * @return 返回用户个人信息
     */
    ServerResponse<UserInfo> queryUserByName(String username);

    /**
     * 根据用户id查询用户个人信息
     * @param userId 用户id
     * @return 返回通用对象
     */
    ServerResponse<UserInfo> queryUserById(Integer userId);

    /**
     * 根据用户id查找用户邮箱
     * where的条件：主表id
     * @param userId 主表的id
     * @return 返回通用对象
     */
    ServerResponse<String> findPasswordByUserId(Integer userId);

    /**
     * 查询邮箱是否存在
     * @param email 邮箱
     * @return 返回查询到的条数
     */
    int checkEmail(String email);

}
