package com.hzq.service.Impl;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.dao.FriendDao;
import com.hzq.dao.UserInfoDao;
import com.hzq.domain.Friend;
import com.hzq.domain.UserInfo;
import com.hzq.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.dc.pr.PRError;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.service.Impl
 * @version: 1.0
 */
@Service("friendService")
public class FriendServiceImpl implements FriendService {
    @Autowired
    private FriendDao friendDao;
    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public ServerResponse<String> insert(Friend friend) {
        if (friendDao.selectFriendByFriendId(friend.getUserId(),friend.getFriendId()) != null) {
            return ServerResponse.createByErrorMessage("已经添加好友，无须再次添加");
        }
        if (friendDao.insert(friend) == 0) {
            return ServerResponse.createByErrorMessage("添加好友失败");
        }
        Integer friendId = friend.getFriendId();
        Integer userId = friend.getUserId();
        UserInfo userInfo = userInfoDao.queryUserById(userId);
        friend.setUserId(friendId);
        friend.setFriendId(userId);
        friend.setFriendAvatar(userInfo.getAvatar());
        friend.setFriendName(userInfo.getNickname());
        if (friendDao.insert(friend) == 0) {
            return ServerResponse.createByErrorMessage("添加好友失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> update(Friend friend) {
        if (friendDao.update(friend) == 0) {
            return ServerResponse.createByErrorMessage("更新好友信息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> delete(Integer id, Integer friendId) {
        if (friendDao.delete(id,friendId) == 0) {
            return ServerResponse.createByErrorMessage("删除好友失败");
        }
        Friend friend = new Friend();
        friend.setFriendId(id);
        friend.setUserId(friendId);
        friend.setIsDelete(id);
        if (friendDao.update(friend) == 0) {
            return ServerResponse.createByErrorMessage("修改好友被删除信息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<List<Friend>> selectAll(Integer id) {
        List<Friend> friends = friendDao.selectAll(id,Const.IS_FRIEND);
        if (friends == null) {
            return ServerResponse.createByErrorMessage("没有查询到好友");
        }
        return ServerResponse.createBySuccess(friends);
    }

    @Override
    public ServerResponse<Friend> selectFriendByFriendName(Integer id, String friendName) {
        Friend friend = friendDao.selectFriendByFriendName(id,friendName);
        if (friend == null) {
            return ServerResponse.createByErrorMessage("查不到该好友的个人信息");
        }
        return ServerResponse.createBySuccess(friend);
    }


}
