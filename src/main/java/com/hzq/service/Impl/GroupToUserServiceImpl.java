package com.hzq.service.Impl;

import com.hzq.common.Const;
import com.hzq.domain.Group;
import com.hzq.handler.ChatWebSocketHandler;
import com.hzq.service.GroupService;
import com.hzq.vo.GroupUserVo;
import com.hzq.vo.ServerResponse;
import com.hzq.dao.GroupToUserDao;
import com.hzq.domain.GroupToUser;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import com.hzq.service.GroupToUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/6
 * @Description: 对用户和群聊，群聊消息的关系进行操作
 * @version: 1.0
 */
@Service("groupToUserService")
public class GroupToUserServiceImpl implements GroupToUserService {

    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupToUserDao groupToUserDao;
    @Autowired
    private ChatWebSocketHandler chat;


    @Override
    public ServerResponse insert(Integer userId, Integer groupId) {
        if (groupToUserDao.checkGroupToUser(userId,groupId) > 0) {
            return ServerResponse.createByErrorMessage("用户已经加入群聊");
        }
        if (groupToUserDao.insert(userId,groupId) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"在群聊内添加用户失败");
        }
        chat.systemAdviceGroupMember(groupId,userId,Const.GROUP_ADD_MEMBER);
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> delete(Integer userId, Integer groupId) {
        Integer groupAdminId = groupService.select(groupId).getData().getGroupAdminId();
        if (userId.equals(groupAdminId)) {
            List<GroupToUser> groupToUsers = groupToUserDao.selectByGroupId(groupId);
            if (groupToUsers.size() == 1) {
                groupService.delete(groupId);
                return ServerResponse.createBySuccess();
            } else {
                Integer id = groupToUsers.get(0).getUserId();
                if (!id.equals(userId)) {
                    groupService.update(new Group(groupId,id));
                } else {
                    id = groupToUsers.get(1).getUserId();
                    groupService.update(new Group(groupId,id));
                }
                groupToUserDao.delete(userId,groupId);
            }
        }
        chat.systemAdviceGroupMember(groupId,userId, Const.GROUP_DELETE_MEMBER);
        return ServerResponse.createBySuccess();
    }


    @Override
    public void update(GroupToUser groupToUser) {
        if(groupToUserDao.update(groupToUser) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"更新用户在群内信息失败");
        }
    }

    @Override
    public ServerResponse<GroupUserVo> selectGroupToUser(Integer userId, Integer groupId) {
        GroupUserVo groupUserVo = groupToUserDao.selectGroupToUser(userId,groupId);
        if(groupUserVo == null) {
            throw CustomGenericException.CreateException(40,"查询不到用户在内的信息");
        }
        return ServerResponse.createBySuccess(groupUserVo);
    }

    @Override
    public ServerResponse<List<GroupToUser>> selectByGroupId(Integer groupId) {
        List<GroupToUser> groupToUsers = groupToUserDao.selectByGroupId(groupId);
        if (groupToUsers == null) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"查询群用户出错");
        }
        return ServerResponse.createBySuccess(groupToUsers);
    }

    @Override
    public ServerResponse<List<GroupUserVo>> select(Integer groupId) {
        List<GroupUserVo> groupUserVos = groupToUserDao.select(groupId);
        if (groupUserVos == null) {
            throw CustomGenericException.CreateException(40,"查询群用户个人信息出错");
        }
        return ServerResponse.createBySuccess(groupUserVos);
    }

}
