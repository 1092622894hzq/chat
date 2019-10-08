package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.domain.Result;
import com.hzq.domain.User;
import com.hzq.service.UserService;
import com.hzq.utils.JwtUil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ServerResponse<Result> login(@RequestBody Map<String,String> map, HttpSession session){
        String username = map.get(Const.USERNAME);
        String password = map.get(Const.PASSWORD);
        ServerResponse<Result> response = userService.login(username,password);
        if (response.isSuccess()) {
            String accessToken = JwtUil.sign(username, response.getData().getUser().getId());
            session.setAttribute(Const.CURRENT_USER,response.getData().getUser());
            response.setAccessToken(accessToken);
            return response;
            }
        return response;
    }

    /**
     * 更新主表的信息
     * @param map 存放新密码和旧密码
     * @return 返回通用对象
     */
    @RequestMapping( value = "/updatePassword", method = RequestMethod.POST)
    public ServerResponse<String> updatePassword(@RequestBody Map<String,String> map, HttpSession session) {
        String newPassword = map.get(Const.NEW_PASSWORD);
        String oldPassword = map.get(Const.OLD_PASSWORD);
        Integer id = ((User)session.getAttribute(Const.CURRENT_USER)).getId();
        return userService.updatePassword(newPassword,oldPassword,id);
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
        if (response.isSuccess()) {
            session.removeAttribute(Const.CURRENT_USER);
            return response;
        }
        return ServerResponse.createByErrorMessage("下线失败");
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
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user.getId().equals(id) && user.getUsername().equals(username)) {
            String accessToken = JwtUil.sign(username, id);
            ServerResponse<String> response = ServerResponse.createBySuccess();
            response.setAccessToken(accessToken);
            return response;
        }
        return ServerResponse.createByErrorMessage("刷新token失败");
    }

}
