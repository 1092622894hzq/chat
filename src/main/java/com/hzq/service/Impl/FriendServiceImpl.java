package com.hzq.service.Impl;

import com.hzq.common.ServerResponse;
import com.hzq.dao.FriendDao;
import com.hzq.dao.UserDao;
import com.hzq.domain.Friend;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.handler.ChatWebSocketHandler;
import com.hzq.service.ApplyService;
import com.hzq.service.FriendService;
import com.hzq.vo.FriendVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 好友
 * @version: 1.0
 */
@Service("friendService")
public class FriendServiceImpl implements FriendService {
    @Autowired
    private FriendDao friendDao;
    @Autowired
    private ChatWebSocketHandler chat;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private UserDao userDao;

    @Override
    public ServerResponse<String> update(Friend friend) {
        if (friendDao.update(friend) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"更新好友信息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> delete(Integer id, Integer friendId) {
        if (id.equals(friendId)) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(),"用户不能删除自己");
        }
        friendDao.delete(id,friendId);
        //更新被好友删除的标志位,可能会更新失败，但是没关系
        friendDao.updateForDelete(friendId,id);
        //删除之前的好友申请记录,不知道是哪个发起的申请，就两种可能都删除
        //但是存在可能，用户删除好友，有添加好友，但是那边好友发现被删除，去删除好友，但是会一起删除了新的申请信息，需要判断申请状态
        if (applyService.checkApply(friendId,id) == 0) {
            applyService.deleteById(id, friendId);
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<List<FriendVo>> selectAll(Integer id) {
        List<FriendVo> friends = friendDao.selectAll(id);
        if (friends == null) {
            return ServerResponse.createByErrorMessage("没有查询到好友");
        }
        return ServerResponse.createBySuccess(friends);
    }

    @Override
    public ServerResponse<FriendVo> selectFriendByFriendName(Integer id, String friendName) {
        FriendVo friend = friendDao.selectFriendByFriendName(id,friendName);
        if (friend == null) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"查不到该好友的个人信息");
        }
        return ServerResponse.createBySuccess(friend);
    }

    @Override
    public ServerResponse<FriendVo> selectFriendByFriendId(Integer userId, Integer friendId) {
        FriendVo friend = friendDao.selectFriendByFriendId(userId,friendId);
        if (friend == null) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"查不到该好友的个人信息");
        }
        return ServerResponse.createBySuccess(friend);
    }

    @Override
    public ServerResponse<String> insertFriendBySelect(Integer userId, Integer friendId) {
        //判断用户是否存在
        if (userDao.checkUserId(userId) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"所要添加的用户不存在");
        }
        //判断是否为好友
        if (friendDao.checkFriend(userId,friendId) > 0) {
            throw  CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"已经添加好友，无须再次添加");
        }
        //添加好友
        if (friendDao.insertFriendBySelect(userId,friendId) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"添加好友失败");
        }
        //通知好友和自己，添加好友成功
        chat.systemAdviceFriend(friendId,userId);
        return ServerResponse.createBySuccess();
    }


}
