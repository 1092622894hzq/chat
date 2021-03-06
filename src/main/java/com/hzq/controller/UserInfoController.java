package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.vo.ServerResponse;
import com.hzq.domain.User;
import com.hzq.domain.UserInfo;
import com.hzq.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 用户个人信息控制层
 * @version: 1.0
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 更新用户个人信息
     * @param userInfo 用户要修改的信息
     * @return 返回通用对象
     */
    @RequestMapping( value = "/update", method = RequestMethod.POST )
    public ServerResponse<String> update(@RequestBody UserInfo userInfo, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        userInfo.setUserId(user.getId());
        return userInfoService.update(userInfo);
    }

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 返回通用对象
     */
    @RequestMapping(value = "/query/{username}", method = RequestMethod.GET)
    public ServerResponse<UserInfo> queryUserByName(@PathVariable String username) {
        return userInfoService.queryUserByName(username);
    }

//    /**
//     * 找回密码
//     * @param id 用户id
//     * @return 返回通用对象
//     */
//    @RequestMapping(value = "/findPassword/{id}", method = RequestMethod.GET)
//    public ServerResponse<String> findPasswordByUserId(@PathVariable Integer id) {
//        return userInfoService.findPasswordByUserId(id);
//    }
}
