package com.hzq.service.Impl;

import com.hzq.common.Const;
import com.hzq.vo.ServerResponse;
import com.hzq.dao.*;
import com.hzq.domain.*;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.service.*;
import com.hzq.utils.JwtUil;
import com.hzq.utils.MD5Util;
import com.hzq.utils.RedisUtil;
import com.hzq.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 用户
 * @version: 1.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private FriendService friendService;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> validResponse = checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setPassword(MD5Util.generate(user.getPassword()));
        if (userDao.insert(user) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(), "注册失败");
        }
        //由触发器自动设定默认个人信息
        //由触发器自动添加自己为好友
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<Result> login(String username, String password) {
        if (userDao.checkUsername(username) == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        User user = userDao.selectByUsername(username);
        if (!MD5Util.verify(password,user.getPassword())) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        updateStatus(Const.ONLINE,user.getId());
        user.setPassword(StringUtils.EMPTY);
        Result result = getAllInfo(user);
        return ServerResponse.createBySuccess("成功",result);
    }


    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (isNotBlank(type)) {
            if (Const.USERNAME.equals(type) && userDao.checkUsername(str) > 0) {
                return ServerResponse.createByErrorMessage("账号已存在,请重新输入");
            }
            if (Const.EMAIL.equals(type) && userInfoDao.checkEmail(str) > 0) {
                return ServerResponse.createByErrorMessage("邮箱已存在,请重新输入");
            }
        } else {
            return ServerResponse.createByErrorMessage("输入参数错误");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<User> selectByUsername(String username) {
        User user = userDao.selectByUsername(username);
        if (user == null) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(),"用户不存在");
        }
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<String> updatePassword(String newPassword, String oldPassword, Integer id) {
        User user = userDao.selectById(id);
        newPassword = MD5Util.generate(newPassword);
        boolean isTrue = MD5Util.verify(oldPassword,user.getPassword());
        boolean b = userDao.updatePassword(newPassword,id) > 0;
        if (isTrue && b) {
            return ServerResponse.createBySuccess();
        }
        throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"更改密码时，原密码错误");
    }

    @Override
    public ServerResponse<String> updateStatus(Integer status, Integer id) {
        if (userDao.updateStatus(status,id) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"更新用户登录状态失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> refreshToken(String username, Integer id, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null || !user.getId().equals(id)) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(),"用户尚未登录");
        }
        if (user.getId().equals(id) && user.getUsername().equals(username)) {
            String accessToken = JwtUil.sign(username, id);
            ServerResponse<String> response = ServerResponse.createBySuccess();
            response.setAccessToken(accessToken);
            response.setSessionId(session.getId());
            return response;
        }
        return ServerResponse.createByErrorMessage("刷新token失败");
    }


    @Override
    public ServerResponse<String> deleteUserById(Integer id) {
        //1. 删除所有申请---有外键关联，所有好友申请都会被删除
        //2. 删除所有好友---有外键关联，所有好友都会被删除
        //3. 删除用户个人信息---有外键关联，自动删除
        //4. 删除私聊消息---有外键关联，自动删除
        //5. 删除用户和群聊关系---有外键关联，自动删除
        //6. 删除redis跟用户相关的存储数据
        redisUtil.remove(id.toString());
        //7. 删除用户
        userDao.deleteUserById(id);
        return ServerResponse.createBySuccess();
    }


    public Result getAllInfo(User user) {
        Result result = new Result();
        result.setUser(user);
        UserInfo userInfo = userInfoDao.queryUserByName(user.getUsername());
        result.setUserInfo(userInfo);
        List<FriendVo> friends = friendService.selectAll(user.getId()).getData();
        result.setFriends(friends);
        List<Group> groups = groupService.selectAll(user.getId()).getData();
        result.setGroups(groups);
        List<ApplyVo> applies = applyService.selectAll(user.getId()).getData();
        result.setApplies(applies);
        return result;
    }

}


















