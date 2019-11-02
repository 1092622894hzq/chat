package com.hzq.service.Impl;


import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.dao.ApplyDao;
import com.hzq.dao.FriendDao;
import com.hzq.domain.*;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.handler.ChatWebSocketHandler;
import com.hzq.service.ApplyService;
import com.hzq.service.FriendService;
import com.hzq.vo.ApplyVo;
import com.hzq.vo.FriendVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

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
    private FriendService friendService;
    @Autowired
    private FriendDao friendDao;
    @Autowired
    private ChatWebSocketHandler chat;

    @Override
    public ServerResponse<String> insert(Apply apply) {
        //1.用户删除了好友又添加好友
        //2.用户删除了好友，好友又添加用户
        //3.用户第一次添加好友，用户未处理，又发起好友申请
        if(friendDao.checkFriend(apply.getFromId(),apply.getToId()) > 0) {  //只要查出用户不是它的朋友即可
            throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(), "已经是好友");
        }
        if (applyDao.checkApply(apply.getFromId(),apply.getToId(),Const.APPLY_REFUSE) > 0) { //查出好友尚未处理申请
            throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(), "请耐心等待，对方还没处理申请");
        }
        if (applyDao.insert(apply) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(), "添加申请失败");
        }
        //向用户发布有申请的消息通知
        chat.systemAdviceApply(apply);
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> delete(Integer fromId, Integer toId, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        applyDao.delete(fromId,toId,user.getId());
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteById(Integer fromId, Integer toId) {
        applyDao.deleteById(fromId,toId);
        return ServerResponse.createBySuccess();
    }


    @Override
    public ServerResponse<String> deleteByUserId(Integer userId) {
        applyDao.deleteByUserId(userId);
        return ServerResponse.createBySuccess();
    }


    @Override
    public ServerResponse<FriendVo> update(Apply apply) {
        //改变申请状态
        if (applyDao.update(apply) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"更新申请失败");
        }
        //同意则添加好友
        if (Const.APPLY_AGREE.equals(apply.getApplyStatus())) {
            //1.用户删除了好友又添加好友
            //2.用户删除了好友，好友又添加用户
            //3.用户第一次添加好友，用户未处理，又发起好友申请
            //对于上述三种情况，直接删除好友，这样避免讨论
            friendDao.deleteTwo(apply.getFromId(),apply.getToId());
            //同时插入用户和好友的关系
            friendService.insertFriendBySelect(apply.getToId(),apply.getFromId());
        }
        return friendService.selectFriendByFriendId(apply.getToId(),apply.getFromId());
    }

    @Override
    public ServerResponse<List<ApplyVo>> selectAll(Integer id) {
        List<ApplyVo> applies = applyDao.selectAll(id);
        if ( applies == null) {
            return ServerResponse.createBySuccessMessage("没有好友可查询");
        }
        return ServerResponse.createBySuccess(applies);
    }

    @Override
    public ServerResponse<ApplyVo> select(Integer fromId, Integer toId) {
        ApplyVo applyVo = applyDao.select(fromId,toId);
        if ( applyVo == null) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"查询不到该申请");
        }
        return ServerResponse.createBySuccess(applyVo);
    }

    @Override
    public int checkApply(Integer fromId, Integer toId) {
        return applyDao.checkApply(fromId,toId,Const.APPLY_UNTREATED);
    }
}
