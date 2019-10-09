package com.hzq.service.Impl;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.dao.*;
import com.hzq.domain.*;
import com.hzq.service.UserService;
import com.hzq.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.service.Impl
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
    private ApplyDao applyDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private GroupMessageContentDao groupMessageContentDao;

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> validResponse = checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setPassword(MD5Util.generate(user.getPassword()));
        if (userDao.insert(user) == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(user.getUsername());
        userInfo.setUserId(user.getId());
        if (userInfoDao.insert(userInfo) == 0) {
            ServerResponse<String> response = deleteUserByPrimaryId(user.getId());
            if (response.isSuccess())
                return ServerResponse.createByErrorMessage("插入个人信息出错");
            else
                return ServerResponse.createByErrorMessage("插入用户个人信息失败并且尝试删除该失败用户同时也失败");
        }
        Friend friend = new Friend();
        friend.setFriendAvatar("default.png");
        friend.setFriendId(Const.AUTHORITY);
        friend.setUserId(user.getId());
        friend.setFriendName(Const.AUTHORITY_NAME);
        if (friendDao.insert(friend) == 0) {
            ServerResponse<String> response = deleteUserByPrimaryId(user.getId());
            if (response.isSuccess())
                return ServerResponse.createByErrorMessage("插入个人信息出错");
            else
                return ServerResponse.createByErrorMessage("插入朋友失败并且尝试删除该用户也失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<Result> login(String username, String password) {
        if (userDao.checkUsername(username) == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        User user = userDao.selectLogin(username);
        boolean isTrue = MD5Util.verify(password,user.getPassword());
        if (!isTrue){
            return ServerResponse.createByErrorMessage("密码错误");
        }
        if (userDao.updateStatus(Const.ONLINE,user.getId()) > 0) {
            System.out.println("成功修改登录状态");
        }
        user.setPassword(StringUtils.EMPTY);
        Result result = new Result();
        result.setUser(user);
        UserInfo userInfo = userInfoDao.queryUserByName(username);
        result.setUserInfo(userInfo);
        List<Friend> friends = friendDao.selectAll(user.getId(),Const.IS_FRIEND);
        result.setFriends(friends);
        List<Group> groups = groupDao.selectAll(user.getId());
        result.setGroups(groups);
        List<Apply> applies = applyDao.selectAll(user.getId());
        Map<Integer, List<Apply>> applyMap = MessageSubgroup(applies,new Apply());
        result.setApplyMap(applyMap);
        List<Message> messages = messageDao.queryUnreadMessageByUserId(user.getId(),Const.MARK_AS_UNREAD);
        Map<Integer, List<Message>> messageMap = MessageSubgroup(messages,new Message());
        result.setMessageMap(messageMap);
        List<GroupMessageContent> groupContents = groupMessageContentDao.selectAllUnread(user.getId());
        Map<Integer, List<GroupMessageContent>> groupContentMap = MessageSubgroup(groupContents,new GroupMessageContent());
        result.setGroupContentMap(groupContentMap);
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
        return ServerResponse.createByErrorMessage("修改失败");
    }

    @Override
    public ServerResponse<String> updateStatus(Integer status, Integer id) {
        if (userDao.updateStatus(status,id) == 0) {
            return ServerResponse.createByErrorMessage("修改用户状态失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteUserByPrimaryId(Integer id) {
        if (userDao.deleteUserByPrimaryId(id) == 0) {
            return ServerResponse.createByErrorMessage("删除用户失败");
        }
        //1.删除用户信息
        if (userInfoDao.deleteUserInfoByPrimaryId(id) == 0) {
            return ServerResponse.createByErrorMessage("删除用户个人失败");
        }
        //2.删除用户和私人聊天的记录
        //3.从群聊中移除用户
        //4.删除好友申请记录
        applyDao.deleteById(id);
        //5.从群聊消息和群用户的关联中移除用户
        //6.用户为群主的直接删除群聊
        //7.删除所有好友
        if (friendDao.deleteById(id) == 0) {
            return ServerResponse.createByErrorMessage("删除用户好友失败");
        }
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

    /**
     * 将申请消息，群聊消息，私聊消息分组
     * @param messages 消息集合
     * @param t 消息类型
     * @param <T> 泛型参数
     * @return 返回分好组的map集合
     */
    private <T> Map<Integer,List<T>> MessageSubgroup(List<T> messages, T t) {
        List<T> listContent = null;
        Integer key = null;
        Map<Integer,List<T>> map = new HashMap<>();
        if (messages != null) {
            for (T message : messages) {
                if (Message.class.equals(t.getClass())) {
                    key = ((Message)message).getMessageFromId();
                } else if (Apply.class.equals(t.getClass())){
                    key = ((Apply)message).getFromId();
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
        return null;
    }
}


















