package com.hzq.service.Impl;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.dao.*;
import com.hzq.domain.*;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.service.ApplyService;
import com.hzq.service.GroupMessageContentService;
import com.hzq.service.MessageService;
import com.hzq.service.UserService;
import com.hzq.utils.JsonUtil;
import com.hzq.utils.JwtUil;
import com.hzq.utils.MD5Util;
import com.hzq.utils.RedisUtil;
import com.hzq.vo.ApplyVo;
import com.hzq.vo.CommonResult;
import com.hzq.vo.FriendVo;
import com.hzq.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
    private FriendDao friendDao;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private GroupMessageContentService groupMessageContentService;
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
        User user = userDao.selectLogin(username);
        if (!MD5Util.verify(password,user.getPassword())) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        updateStatus(Const.ONLINE,user.getId());

        user.setPassword(StringUtils.EMPTY);
        Result result = new Result();
        result.setUser(user);
        UserInfo userInfo = userInfoDao.queryUserByName(username);
        result.setUserInfo(userInfo);
        List<FriendVo> friends = friendDao.selectAll(user.getId());
        result.setFriends(friends);
        List<Group> groups = groupDao.selectAll(user.getId());
        result.setGroups(groups);
        List<ApplyVo> applies = applyService.selectAll(user.getId()).getData();
        result.setApplies(applies);
        Map<Integer, List<Message>> messageMap = messageService.queryUnreadMessageByUserId(user.getId()).getData();
        result.setMessageMap(messageMap);
        Map<Integer, List<GroupMessageContent>> groupContentMap = groupMessageContentService.selectAllUnread(user.getId()).getData();
        result.setGroupContentMap(groupContentMap);
        if (redisUtil.exists(user.getId().toString())) {
            CommonResult commonResult = JsonUtil.getObjFromJson((String) redisUtil.get(user.getId().toString()),CommonResult.class);
            result.setCommonResult(commonResult);
            //把消息从redis中删除
            redisUtil.remove(user.getId().toString());
        }
        return ServerResponse.createBySuccess("成功",result);
    }

    @Override
    public ServerResponse<String> updatePassword(String newPassword, String oldPassword, Integer id) {
        User user = userDao.selectUserById(id);
        newPassword = MD5Util.generate(newPassword);
        boolean isTrue = MD5Util.verify(oldPassword,user.getPassword());
        if (isTrue && userDao.updatePassword(newPassword,id) > 0) {
            return ServerResponse.createBySuccess();
        }
        throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"更改密码时发生错误,很可能密码错误");
    }

    @Override
    public ServerResponse<String> updateStatus(Integer status, Integer id) {
        if (userDao.updateStatus(status,id) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"更新用户登录状态失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteUserByPrimaryId(Integer id) {
        if (userDao.deleteUserByPrimaryId(id) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"删除用户失败");
        }
        //删除所有申请
        applyService.deleteByUserId(id);
        //通知好友被删除了
        return ServerResponse.createBySuccess();
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
    public  <T> Map<Integer,List<T>> MessageSubgroup(List<T> messages, T t) {
        if (messages == null) return null;
        List<T> listContent;
        Integer key ;
        Map<Integer,List<T>> map = new HashMap<>();
        for (T message : messages) {
            if (Message.class.equals(t.getClass())) {
                key = ((Message)message).getMessageFromId();
            } else if (ApplyVo.class.equals(t.getClass())){
                key = ((ApplyVo)message).getFromId();
            } else {
                key = ((GroupMessageContent)message).getGroupId();
            }
            if (map.get(key) != null) {
                map.get(key).add(message);
            } else {
                listContent = new ArrayList<>();
                listContent.add(message);
                map.put(key, listContent);
            }
        }
        return map;
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
    public ServerResponse<User> selectByUsername(String username) {
        User user = userDao.selectLogin(username);
        if (user == null) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(),"用户不存在");
        }
        return ServerResponse.createBySuccess(user);
    }


}


















