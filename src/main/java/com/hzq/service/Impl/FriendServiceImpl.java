package com.hzq.service.Impl;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.dao.FriendDao;
import com.hzq.domain.Friend;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.service.FriendService;
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

    //借助mybatis同时执行多条sql完成两部分的好友信息的插入
    @Override
    public ServerResponse<String> insert(Friend friend) {
        if (friendDao.selectFriendByFriendId(friend.getUserId(),friend.getFriendId()) != null) {
            return ServerResponse.createByErrorMessage("已经添加好友，无须再次添加");
        }
        if (friendDao.insert(friend) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"添加好友失败");
        }
        //告诉安卓，好友添加成功
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> update(Friend friend) {
        if (friendDao.update(friend) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"更新好友信息失败");
        }
        return ServerResponse.createBySuccess();
    }

    //借助mybatis同时执行多条sql设定被好友删除的标志位
    @Override
    public ServerResponse<String> delete(Integer id, Integer friendId) {
        if (friendDao.delete(id,friendId) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"删除好友失败");
        }
        //通知好友被删除了
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
