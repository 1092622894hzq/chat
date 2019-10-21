package com.hzq.service.Impl;


import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.dao.ApplyDao;
import com.hzq.dao.UserInfoDao;
import com.hzq.domain.*;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.handler.ChatWebSocketHandler;
import com.hzq.service.ApplyService;
import com.hzq.service.FriendService;
import com.hzq.service.UserInfoService;
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
    private ChatWebSocketHandler chat;

    @Override
    public ServerResponse<String> insert(Apply apply) {
        if (applyDao.checkApply(apply.getFromId(),apply.getToId(),Const.APPLY_REFUSE) > 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(), "已经是好友，或者对方还没处理申请");
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
        if (applyDao.delete(fromId,toId,user.getId()) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"删除申请失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteById(Integer fromId, Integer toId) {
        if (applyDao.deleteById(fromId,toId) == 0) {
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
    public ServerResponse<FriendVo> update(Apply apply) {
        if (applyDao.update(apply) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"更新申请失败");
        }
        if (Const.APPLY_AGREE.equals(apply.getApplyStatus())) {
            friendService.insertFriendBySelect(apply.getToId(),apply.getFromId());
            //添加好友
//            UserInfo userInfo = userInfoDao.queryUserById(apply.getFromId());
//            if (userInfo != null) {
//                Friend friend = new Friend();
//                friend.setFriendName(userInfo.getNickname());
//                friend.setFriendId(apply.getToId());
//                friend.setUserId(apply.getFromId());
//                friendService.insert(friend);
//            }
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
}
