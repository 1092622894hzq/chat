package com.hzq.service.Impl;

import com.hzq.common.Const;
import com.hzq.utils.VerifyUtil;
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
import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 用户
 * @version: 1.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupToUserService groupToUserService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ServerResponse<String> register(User user) {
        //验证格式
        if (!VerifyUtil.isStandardFormUsername(user.getUsername())) {
            return ServerResponse.createByErrorMessage("用户名格式不对");
        }
        if (!VerifyUtil.isStandardFormPassword(user.getPassword())) {
            return ServerResponse.createByErrorMessage("密码格式不对");
        }
        if (userDao.checkUsername(user.getUsername()) > 0) {
            return ServerResponse.createByErrorMessage("用户名已经存在");
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
        //验证格式
        if (!VerifyUtil.isStandardFormUsername(username)) {
            return ServerResponse.createByErrorMessage("用户名格式不对");
        }
        if (!VerifyUtil.isStandardFormPassword(password)) {
            return ServerResponse.createByErrorMessage("密码格式不对");
        }
        if (userDao.checkUsername(username) == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        User user = userDao.selectByUsername(username);
        if (!MD5Util.verify(password,user.getPassword())) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        if (userDao.updateStatus(Const.ONLINE,user.getId()) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"更新用户状态失败");
        }
        user.setPassword(StringUtils.EMPTY);
        Result result = getAllInfo(user);
        return ServerResponse.createBySuccess("成功",result);
    }

    @Override
    public ServerResponse<User> selectByUsername(String username) {
        if (!VerifyUtil.isStandardFormUsername(username)) {
            return ServerResponse.createByErrorMessage("账号格式不对");
        }
        User user = userDao.selectByUsername(username);
        if (user == null) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(),"用户不存在");
        }
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<User> selectById(Integer id) {
        User user = userDao.selectById(id);
        if (user == null) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(),"用户不存在");
        }
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<String> updatePassword(String newPassword, String oldPassword, Integer id) {
        User user = userDao.selectById(id);
        if (MD5Util.verify(oldPassword,user.getPassword())) {
            return update(newPassword,id);
        }
        return ServerResponse.createByErrorMessage("更改密码时，原密码错误");
    }

    @Override
    public ServerResponse<String> update(String password, Integer id) {
        System.out.println("密码 "+password+"    id "+id);
        if(!VerifyUtil.isStandardFormPassword(password)) {
            return ServerResponse.createByErrorMessage("密码格式不对");
        }
        password = MD5Util.generate(password);
        if (userDao.updatePassword(password,id) > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("更新密码失败");
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
        if (user == null || !user.getId().equals(id) || !user.getUsername().equals(username)) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(),"用户尚未登录");
        }
        String accessToken = JwtUil.sign(username, id);
        ServerResponse<String> response = ServerResponse.createBySuccess();
        response.setAccessToken(accessToken);
        response.setSessionId(session.getId());
        return response;
    }


    @Override
    public ServerResponse<String> deleteUserById(Integer id) {
        //1. 删除所有申请---有外键关联，所有好友申请都会被删除
        //2. 删除所有好友---有外键关联，所有好友都会被删除
        //3. 删除用户个人信息---有外键关联，自动删除
        //4. 删除私聊消息---有外键关联，自动删除
        //5. 删除用户和群聊关系---有外键关联，自动删除
        //6. 更新被所有好友删除的标志位
        friendService.updateAllFriend(id);
        //7. 删除redis跟用户相关的存储数据
        String pattern = "*-"+id.toString()+"-*";
        redisUtil.remove(pattern);
        //8. 需要判断用户是否为群主
       ServerResponse<List<Group>> response = groupService.selectAll(id);
        if (response.isSuccess()) {
            List<Group> groups = response.getData();
            for (Group g : groups) {
                groupToUserService.delete(id,g.getId());
            }
        }
        //9. 删除用户
        userDao.deleteUserById(id);
        return ServerResponse.createBySuccess();
    }

    @Override
    public int checkUserId(Integer id) {
        return userDao.checkUserId(id);
    }

    @Override
    public ServerResponse<String> checkUsername(String username) {
        if (VerifyUtil.isStandardFormUsername(username)) {
            return ServerResponse.createByErrorMessage("用户名格式不对");
        }
        if (userDao.checkUsername(username) > 0) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("用户不存在");
    }


    private Result getAllInfo(User user) {
        Result result = new Result();
        result.setUser(user);
        UserInfo userInfo = userInfoService.queryUserByName(user.getUsername()).getData();
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


















