package com.hzq.service.Impl;


import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.dao.ApplyDao;
import com.hzq.dao.UserInfoDao;
import com.hzq.domain.Apply;
import com.hzq.domain.Friend;
import com.hzq.domain.UserInfo;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.service.ApplyService;
import com.hzq.service.FriendService;
import com.hzq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: 申请
 * @version: 1.0
 */
@Service("applyService")
public class ApplyServiceImpl implements ApplyService {

    @Autowired
    private ApplyDao applyDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private FriendService friendService;

    @Override
    public ServerResponse<String> insert(Apply apply) {
        if (applyDao.insert(apply) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(), "添加申请失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteById(Integer id) {
        if (applyDao.deleteById(id) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(), "删除申请失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteByUserId(Integer userId) {
        if (applyDao.deleteByUserId(userId) == 0) {
            return ServerResponse.createBySuccessMessage("删除所有申请失败");
        }
        return ServerResponse.createBySuccess();
    }


    @Override
    public ServerResponse<String> update(Apply apply) {
        if (applyDao.update(apply) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"更新申请失败");
        }
        if (Const.APPLY_AGREE.equals(apply.getApplyStatus())) {
            UserInfo userInfo = userInfoDao.queryUserById(apply.getFromId());
            if (userInfo != null) {
                Friend friend = new Friend();
                friend.setFriendName(userInfo.getNickname());
                friend.setFriendId(apply.getToId());
                friend.setUserId(apply.getFromId());
                friendService.insert(friend);
            }
        }
        //告诉安卓申请通过，或者拒绝

        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<Map<Integer,List<Apply>>> selectAll(Integer id) {
        List<Apply> applies = applyDao.selectAll(id);
        if ( applies == null) {
            return ServerResponse.createBySuccessMessage("没有好友可查询");
        }
        Map<Integer,List<Apply>> map = userService.MessageSubgroup(applies,new Apply());
        return ServerResponse.createBySuccess(map);
    }
}
