package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.common.CustomGenericException;
import com.hzq.common.ServerResponse;
import com.hzq.domain.User;
import com.hzq.domain.UserInfo;
import com.hzq.service.UserInfoService;
import com.hzq.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 用户个人信息控制层
 * @version: 1.0
 */
@Controller
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 插入用户个人信息
     * @param session 一次会话
     * @param userInfo 个人信息
     * @return 返回通用对象
     */
    @RequestMapping( value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> insert(@RequestBody UserInfo userInfo, HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        userInfo.setUserId(user.getId());
        return userInfoService.insert(userInfo);
    }

    /**
     * 删除用户信息
     * @param userId 用户userId
     * @return 返回通用结果
     */
    @RequestMapping( value = "/delete/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> deleteUserInfoByPrimaryId(@PathVariable Integer userId) {
        return userInfoService.deleteUserInfoByPrimaryId(userId);
    }

    /**
     * 更新用户个人信息
     * @param session 一次会话
     * @param userInfo 用户要修改的信息
     * @return 返回通用对象
     */
    @RequestMapping( value = "/update", method = RequestMethod.POST )
    @ResponseBody
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

    /**
     * 处理上传头像
     * @param session 一次会话
     * @param avatar 头像图片
     * @return 返回通用对象
     */
    @RequestMapping( value = "/updateAvatar", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> updateAvatar(@RequestPart("avatar")MultipartFile avatar, HttpSession session){
            String fileName = System.currentTimeMillis()+avatar.getOriginalFilename();
            try {
                File dir = new File(Const.AVATAR_PATH,fileName);
                FileUtil.ByteToPhoto(avatar.getBytes(),dir);
            } catch (Exception e) {
                throw new CustomGenericException(40, "上传文件出错了");
            }
            User user = (User) session.getAttribute(Const.CURRENT_USER);
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(user.getId());
            userInfo.setAvatar(fileName);
            ServerResponse<String> response = userInfoService.update(userInfo);
            response.setData(fileName);
            return response;
        }

    /**
     * 找回密码
     * @param id 用户id
     * @return 返回通用对象    -----暂未完成
     */
    @RequestMapping(value = "/findPassword/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> findPasswordByUserId(@PathVariable Integer id) {
        return userInfoService.findPasswordByUserId(id);
    }

}
