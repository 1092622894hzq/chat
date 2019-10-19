package com.hzq.service.Impl;


import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.dao.ApplyDao;
import com.hzq.dao.MessageDao;
import com.hzq.dao.UserInfoDao;
import com.hzq.domain.*;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.handler.ChatWebSocketHandler;
import com.hzq.service.ApplyService;
import com.hzq.service.FriendService;
import com.hzq.service.UserService;
import com.hzq.utils.JsonUtil;
import com.hzq.utils.RedisUtil;
import com.hzq.vo.ApplyVo;
import com.hzq.vo.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
    @Autowired
    private ChatWebSocketHandler chat;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ServerResponse<String> insert(Apply apply) {
        if (applyDao.insert(apply) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(), "添加申请失败");
        }
        Timestamp time = new Timestamp(System.currentTimeMillis());
        apply.setGmtCreate(time);
        apply.setGmtModified(time);
        apply.setApplyStatus(Const.APPLY_UNTREATED);
        //通知安卓有好友申请
        Integer toId = apply.getToId();
        Content content = new Content();
        content.setNotice(Const.APPLY);
        content.setTime(time);
        ApplyVo applyVo = select(apply.getFromId(),apply.getToId()).getData();
        content.setMessage(JsonUtil.toJson(applyVo));
        if (!chat.isOnline(toId)) {
            redisUtil.appendObj(toId.toString(),content);
        } else {
            chat.sendMessageToUser(toId,content);
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
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<Map<Integer,List<ApplyVo>>> selectAll(Integer id) {
        List<ApplyVo> applies = applyDao.selectAll(id);
        if ( applies == null) {
            return ServerResponse.createBySuccessMessage("没有好友可查询");
        }
        Map<Integer,List<ApplyVo>> map = userService.MessageSubgroup(applies,new ApplyVo());
        return ServerResponse.createBySuccess(map);
    }

    @Override
    public ServerResponse<ApplyVo> select(Integer fromId, Integer toId) {
        ApplyVo applyVo = applyDao.select(fromId,toId);
        if ( applyVo == null) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"查询不到该申请");
        }
        return ServerResponse.createBySuccess(applyVo);
    }
}
