package com.hzq.service;

import com.hzq.common.ServerResponse;
import com.hzq.domain.Result;
import com.hzq.domain.User;

import java.sql.SQLTransactionRollbackException;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: service接口
 * @version: 1.0
 */
public interface UserService {

    /**
     * 用户注册
     * @param user 用户
     * @return 返回通用对象
     */
    ServerResponse<String> register(User user);

    /**
     * 用户登录
     * @param username 用户账号
     * @param password 用户密码
     * @return 返回通用对象
     */
    ServerResponse<Result> login(String username, String password);

    /**
     * 更新用户密码
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     * @param id 主键id
     * @return 返回通用对象
     */
    ServerResponse<String> updatePassword(String newPassword, String oldPassword, Integer id);

    /**
     * 更新用户状态
     * @param status 状态
     * @param id 用户id
     * @return 返回通用结果
     */
    ServerResponse<String> updateStatus(Integer status, Integer id);

    /**
     * 根据id删除用户
     * @param id 用户id
     * @return 返回通用对象
     */
    ServerResponse<String> deleteUserByPrimaryId(Integer id);

    /**
     * 检查用户名和邮箱是否存在
     * @param str 检查的用户名或者邮箱
     * @param type 类型
     * @return 返回通用对象
     */
    ServerResponse<String> checkValid(String str, String type);

}























