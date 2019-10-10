package com.hzq.service.Impl;

import com.hzq.common.ServerResponse;
import com.hzq.dao.GroupMessageToUserDao;
import com.hzq.dao.GroupToUserDao;
import com.hzq.domain.GroupToUser;
import com.hzq.service.GroupToUserService;
import com.hzq.vo.GroupMessageAndGroupToUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/6
 * @Description: com.hzq.service.Impl
 * @version: 1.0
 */
@Service("groupToUserService")
public class GroupToUserServiceImpl implements GroupToUserService {

    @Autowired
    private GroupToUserDao groupToUserDao;

    @Override
    public ServerResponse<String> insert(GroupToUser groupToUser) {
        if (groupToUserDao.insert(groupToUser) == 0) {
            return ServerResponse.createByErrorMessage("在群聊内添加用户失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> delete(Integer groupUserId, Integer groupId) {
        if (groupToUserDao.delete(groupUserId,groupId) == 0) {
            return ServerResponse.createByErrorMessage("从群聊中删除用户失败");
        }

        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteById(Integer id) {
        if (groupToUserDao.deleteById(id) == 0) {
            return ServerResponse.createByErrorMessage("从群里删除用户失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> update(GroupToUser groupToUser) {
        if(groupToUserDao.update(groupToUser) == 0) {
            return ServerResponse.createByErrorMessage("更新用户在群内信息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<GroupToUser> selectGroupToUser(Integer groupUserId, Integer groupId) {
        GroupToUser groupToUser = groupToUserDao.selectGroupToUser(groupUserId,groupId);
        if(groupToUser == null) {
            return ServerResponse.createByErrorMessage("查询不到用户在内的信息");
        }
        return ServerResponse.createBySuccess(groupToUser);
    }

    @Override
    public ServerResponse<List<GroupToUser>> selectByGroupId(Integer groupId) {
        List<GroupToUser> groupToUsers = groupToUserDao.selectByGroupId(groupId);
        if (groupToUsers == null) {
            return ServerResponse.createByErrorMessage("查询群用户出错");
        }
        return ServerResponse.createBySuccess(groupToUsers);
    }

    @Override
    public ServerResponse<List<GroupMessageAndGroupToUser>> select(Integer groupId) {
        List<GroupMessageAndGroupToUser> groupMessageAndGroupToUsers = groupToUserDao.select(groupId);
        if (groupMessageAndGroupToUsers == null) {
            return ServerResponse.createByErrorMessage("查询群用户出错");
        }
        return ServerResponse.createBySuccess(groupMessageAndGroupToUsers);
    }
}
