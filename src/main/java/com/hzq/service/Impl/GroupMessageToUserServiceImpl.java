package com.hzq.service.Impl;

import com.hzq.common.ServerResponse;
import com.hzq.dao.GroupMessageToUserDao;
import com.hzq.domain.GroupMessageToUser;
import com.hzq.service.GroupMessageToUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/10/4
 * @Description: com.hzq.service.Impl
 * @version: 1.0
 */
@Service("groupMessageToUserService")
public class GroupMessageToUserServiceImpl implements GroupMessageToUserService {

    @Autowired
    private GroupMessageToUserDao messageToUserDao;


    @Override
    public ServerResponse<String> insert(GroupMessageToUser messageToUser) {
        if (messageToUserDao.insert(messageToUser) == 0) {
            return ServerResponse.createByErrorMessage("用户关联群聊消息失败");
        }
        return ServerResponse.createBySuccess();
    }


    @Override
    public ServerResponse<String> update(Integer userId, Integer groupMessageId, Timestamp time) {
        if (messageToUserDao.update(userId,groupMessageId,time) == 0) {
            return ServerResponse.createByErrorMessage("更新群聊消息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> delete(Integer userId) {
        if (messageToUserDao.delete(userId) == 0) {
            return ServerResponse.createByErrorMessage("删除群用户与群消息的关联失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteByGroupMessageAndUserId(Integer userId, Integer groupMessageId, Timestamp time) {
        if (messageToUserDao.update(userId,groupMessageId,time) == 0) {
            return ServerResponse.createByErrorMessage("删除群消息失败");
        }
        return ServerResponse.createBySuccess();
    }
}
