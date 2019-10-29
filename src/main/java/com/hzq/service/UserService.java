package com.hzq.service;

import com.hzq.common.ServerResponse;
import com.hzq.vo.Result;
import com.hzq.domain.User;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

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
     * 检查用户名和邮箱是否存在
     * @param str 检查的用户名或者邮箱
     * @param type 类型
     * @return 返回通用对象
     */
    ServerResponse<String> checkValid(String str, String type);

    /**
     * 将申请消息，群聊消息，私聊消息分组
     * @param messages 消息集合
     * @param t 消息类型
     * @param <T> 泛型参数
     * @return 返回分好组的map集合
     */
    <T> Map<Integer, List<T>> MessageSubgroup(List<T> messages, T t);

    /**
     * 根据用户名和用户id刷新token
     * @param session 一次会话
     * @param username 用户名
     * @param id 用户id
     * @return 返回通用结果
     */
    ServerResponse<String> refreshToken(String username, Integer id, HttpSession session);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 返回通用对象
     */
    ServerResponse<User> selectByUsername(String username);

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
     * 根据用户id删除用户
     * @param id 用户id
     * @return 返回通用对象
     */
    ServerResponse<String> deleteUserById(Integer id);
}























