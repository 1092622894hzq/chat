package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.vo.ServerResponse;
import com.hzq.vo.Result;
import com.hzq.domain.User;
import com.hzq.service.UserService;
import com.hzq.utils.JwtUil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * @Auther: blue
 * @Date: 2019/10/1
 * @Description: 用户主表的操作
 * @version: 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册账号
     * @param user 注册的信息
     * @return 返回通用对象
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ServerResponse<String> register(@RequestBody User user) {
        return userService.register(user);
    }

    /**
     * 根据账号密码登录
     * @param map map中有账号和密码
     * @param session 一次会话
     * @return 返回通用对象
     */
    @RequestMapping( value = "/login", method = RequestMethod.POST)
    public ServerResponse login(@RequestBody Map<String,String> map, HttpSession session, HttpServletResponse resp){
        String username = map.get(Const.USERNAME);
        String password = map.get(Const.PASSWORD);
        ServerResponse<Result> response = userService.login(username,password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,response.getData().getUser());
            session.setMaxInactiveInterval(-1);
            String accessToken = JwtUil.sign(username,response.getData().getUser().getId());
            resp.setHeader("Access-Control-Allow-Origin","*");
            response.setAccessToken(accessToken);
            response.setSessionId("JSESSIONID="+session.getId());
            return response;
        }
        return response;
    }

    /**
     * 更新密码
     * @param map 存放新密码和旧密码
     * @return 返回通用对象
     */
    @RequestMapping( value = "/updatePassword", method = RequestMethod.POST)
    public ServerResponse updatePassword(@RequestBody Map<String,String> map, HttpSession session) {
        String newPassword = map.get(Const.NEW_PASSWORD);
        String oldPassword = map.get(Const.OLD_PASSWORD);
        Integer id = ((User)session.getAttribute(Const.CURRENT_USER)).getId();
        return userService.updatePassword(newPassword,oldPassword,id);
    }

    /**
     * 更新通过密保问题后修改的密码
     * @param user 密码和id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ServerResponse update(@RequestBody User user) {
        return userService.update(user.getPassword(),user.getId());
    }

    /**
     * 更新状态
     * @param status 状态
     * @param id 用户id
     * @return 返回通用对象
     */
    @RequestMapping( value = "/updateStatus/{id}/{status}", method = RequestMethod.GET)
    public ServerResponse<User> updateStatus(@PathVariable Integer status, @PathVariable Integer id,HttpSession session) {
        userService.updateStatus(status,id);
        ServerResponse<User> response = userService.selectById(id);
        response.getData().setPassword(StringUtils.EMPTY);
        session.setAttribute(Const.CURRENT_USER,response.getData());
        return response;
    }

    /**
     * 查询用户主表信息
     * @param username 用户名
     * @return 返回通用对象
     */
    @RequestMapping(value = "/select/{username}", method = RequestMethod.GET)
    public ServerResponse<User> select(@PathVariable String username) {
        ServerResponse<User> response = userService.selectByUsername(username);
        response.getData().setPassword(StringUtils.EMPTY);
        return response;
    }


    /**
     * 退出登录
     * @param session 一次会话
     * @param id 用户id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/logout/{id}", method = RequestMethod.GET)
    public ServerResponse<String> logout(@PathVariable Integer id, HttpSession session){
        ServerResponse<String> response = userService.updateStatus(Const.OFFLINE,id);
        session.invalidate();
        return response;
    }

    /**
     * 刷新token
     * @param username 用户名
     * @param id 用户id
     * @param session 一次会话
     * @return 返回通用对象
     */
    @RequestMapping(value = "/refreshToken/{username}/{id}", method = RequestMethod.GET)
    public ServerResponse<String> refreshToken(@PathVariable String username, @PathVariable Integer id, HttpSession session) {
        return userService.refreshToken(username,id,session);
    }

    /**
     * 删除用户
     * @param id 用户id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/delete/{id}")
    public ServerResponse<String> deleteUserById(@PathVariable Integer id, HttpSession session) {
        ServerResponse<String> response =  userService.deleteUserById(id);
        if (response.isSuccess()) {
            session.invalidate();
        }
        return response;
    }

    /**
     * 检验用户是否存在
     * @param username 用户名
     * @return 返回通用对象
     */
    @RequestMapping(value = "/check/{username}", method = RequestMethod.GET)
    public ServerResponse<String> checkUsername(@PathVariable String username) {
        return userService.checkUsername(username);
    }

}
