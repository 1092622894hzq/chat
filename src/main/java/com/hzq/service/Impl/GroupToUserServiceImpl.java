package com.hzq.service.Impl;

import com.hzq.common.ServerResponse;
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
    private GroupToUserDao groupToUserDao;


    @Override
    public ServerResponse<String> insert(GroupToUser groupToUser) {
        if (groupToUserDao.checkGroupToUser(groupToUser.getUserId(),groupToUser.getGroupId()) > 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.USER_ERROR.getCode(),"用户已经加入群聊");
        }
        if (groupToUserDao.insert(groupToUser) == 0) {
            throw CustomGenericException.CreateException(ResponseCodeEnum.ERROR.getCode(),"在群聊内添加用户失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> delete(Integer userId, Integer groupId) {
        groupToUserDao.delete(userId,groupId);
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteById(Integer id) {
        groupToUserDao.deleteById(id);
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> updateById(GroupToUser groupToUser) {
        if(groupToUserDao.updateById(groupToUser) == 0) {
            return ServerResponse.createByErrorMessage("更新用户在群内信息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<GroupToUser> selectGroupToUser(Integer userId, Integer groupId) {
        GroupToUser groupToUser = groupToUserDao.selectGroupToUser(userId,groupId);
        if(groupToUser == null) {
            throw CustomGenericException.CreateException(40,"查询不到用户在内的信息");
        }
        return ServerResponse.createBySuccess(groupToUser);
    }

    @Override
    public ServerResponse<List<GroupToUser>> selectByGroupId(Integer groupId) {
        List<GroupToUser> groupToUsers = groupToUserDao.selectByGroupId(groupId);
        if (groupToUsers == null) {
            throw CustomGenericException.CreateException(40,"查询群用户出错");
        }
        return ServerResponse.createBySuccess(groupToUsers);
    }

}
