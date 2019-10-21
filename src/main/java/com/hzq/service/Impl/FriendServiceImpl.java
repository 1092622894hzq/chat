package com.hzq.service.Impl;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.dao.FriendDao;
import com.hzq.dao.UserDao;
import com.hzq.domain.Content;
import com.hzq.domain.Friend;
import com.hzq.domain.UserInfo;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.handler.ChatWebSocketHandler;
import com.hzq.service.ApplyService;
import com.hzq.service.FriendService;
import com.hzq.service.UserInfoService;
import com.hzq.service.UserService;
import com.hzq.utils.RedisUtil;
import com.hzq.vo.FriendVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
    private UserInfoService userInfoService;
    @Autowired
    private ChatWebSocketHandler chat;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private UserDao userDao;

//    //借助mybatis同时执行多条sql完成两部分的好友信息的插入
//    @Override
//    public ServerResponse<String> insert(Friend friend) {
//        if (friendDao.selectFriendByFriendId(friend.getUserId(),friend.getFriendId()) != null) {
//            return ServerResponse.createByErrorMessage("已经添加好友，无须再次添加");
//        }
//        if (friendDao.insert(friend) == 0) {
//            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"添加好友失败");
//        }
//        //告诉安卓，好友添加成功
//        //好友id
//        Integer friendId = friend.getFriendId();
//        //用户id
//        Integer userId = friend.getUserId();
//        Content content = new Content();
//        content.setNotice(Const.FRIEND);
//        content.setTime(new Timestamp(System.currentTimeMillis()));
//        content.setMessage("我是"+friend.getFriendName());
//        //发送给自己
//        chat.sendMessageToUser(userId,content);
//        //发送给好友
//        content.setMessage("我通过了你的朋友验证请求，现在我们可以开始聊天了");
//        if (chat.isOnline(friendId)) {
//            redisUtil.appendObj(friendId.toString(),content);
//        } else {
//            chat.sendMessageToUser(friendId,content);
//        }
//        return ServerResponse.createBySuccess();
//    }

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
        if (friendDao.delete(id,friendId) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"删除好友失败");
        }
        //更新被好友删除的标志位
        Friend friend = new Friend();
        friend.setUserId(friendId);
        friend.setFriendId(id);
        friend.setIsDelete(id);
        friendDao.update(friend); //可能会更新失败，但是没关系
        //删除之前的好友申请记录
        applyService.deleteById(id, friendId);
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
            return ServerResponse.createByErrorMessage("已经添加好友，无须再次添加");
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
