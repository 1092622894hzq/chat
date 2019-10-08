package com.hzq.service.Impl;

import com.hzq.common.ServerResponse;
import com.hzq.dao.UserInfoDao;
import com.hzq.domain.UserInfo;
import com.hzq.service.UserInfoService;
import com.hzq.utils.RandomUtil;
import com.hzq.utils.SendEmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.service.Impl
 * @version: 1.0
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public ServerResponse<String> insert(UserInfo userInfo) {
       if (userInfoDao.insert(userInfo) == 0){
           return ServerResponse.createByErrorMessage("插入个人信息失败");
       }
       return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteUserInfoByPrimaryId(Integer userId) {
        if (userInfoDao.deleteUserInfoByPrimaryId(userId) == 0) {
            return ServerResponse.createByErrorMessage("删除用户个人信息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> update(UserInfo userInfo) {
        if (userInfo.getEmail() != null && userInfoDao.checkEmail(userInfo.getEmail()) > 0) {
            return ServerResponse.createByErrorMessage("邮箱已经被注册，请重新输入新邮箱");
        }
        if (userInfoDao.update(userInfo) == 0) {
            return ServerResponse.createByErrorMessage("修改个人信息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<UserInfo> queryUserByName(String username) {
        UserInfo userInfo = userInfoDao.queryUserByName(username);
        if (userInfo == null) {
            return ServerResponse.createByErrorMessage("查询不到用户信息");
        }
        return ServerResponse.createBySuccess(userInfo);
    }

    @Override
    public ServerResponse<String> findPasswordByUserId(Integer userId) {
        String email = userInfoDao.selectEmailByUserId(userId);
        if (email == null) {
            return ServerResponse.createByErrorMessage("该用户没有注册邮箱");
        }
        String number = RandomUtil.CreateRandom(6);
        String subject = "找回密码";
        String content = "验证码： "+number;
        SendEmailUtil.sendEmail(email,subject,content);
        return ServerResponse.createBySuccess(number);
    }


}
