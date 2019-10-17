package com.hzq.service.Impl;

import com.hzq.common.ServerResponse;
import com.hzq.dao.GroupMessageContentDao;
import com.hzq.dao.GroupToUserDao;
import com.hzq.domain.GroupMessageContent;
import com.hzq.domain.GroupToUser;
import com.hzq.service.GroupMessageContentService;
import com.hzq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: 群聊消息
 * @version: 1.0
 */
@Service("groupMessageContentService")
public class GroupMessageContentServiceImpl implements GroupMessageContentService {

    @Autowired
    private GroupMessageContentDao messageContentDao;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupToUserDao groupToUserDao;

    @Override
    public ServerResponse<String> insert(GroupMessageContent groupMessageContent) {
        if (messageContentDao.insert(groupMessageContent) == 0) {
            return ServerResponse.createByErrorMessage("存放群聊消息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<List<GroupMessageContent>> selectAll(Integer id) {
        List<GroupMessageContent> groupMessageContents = messageContentDao.selectAll(id);
        if (groupMessageContents == null) {
            return ServerResponse.createByErrorMessage("没有查询到群聊记录");
        }
        return ServerResponse.createBySuccess(groupMessageContents);
    }

    @Override
    public ServerResponse<String> delete(Integer id) {
        if (messageContentDao.delete(id) == 0) {
            return ServerResponse.createByErrorMessage("删除群聊失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<List<GroupMessageContent>> selectUnread(Integer userId, Integer groupId) {
        List<GroupMessageContent> messageContents = messageContentDao.selectUnread(userId,groupId);
        if (messageContents == null) {
            return ServerResponse.createBySuccessMessage("查询不到未读消息");
        }
        //更新时间
        return ServerResponse.createBySuccess(messageContents);
    }

    @Override
    public ServerResponse<Map<Integer,List<GroupMessageContent>>> selectAllUnread(Integer userId) {
        List<GroupMessageContent> messageContents = messageContentDao.selectAllUnread(userId);
        if (messageContents == null) {
            return ServerResponse.createBySuccessMessage("查询不到未读消息");
        }
        Map<Integer,List<GroupMessageContent>> map = userService.MessageSubgroup(messageContents,new GroupMessageContent());
        //更新时间
        for (Integer key : map.keySet()) {
            //最新的未读的消息的id
            Integer messageId = map.get(key).get(map.get(key).size()-1).getId();
            Integer groupId = map.get(key).get(map.get(key).size()-1).getGroupId();
            GroupToUser groupToUser = new GroupToUser();
            groupToUser.setUserId(userId);
            groupToUser.setGroupId(groupId);
            groupToUser.setGroupMessageId(messageId);
            groupToUserDao.update(groupToUser);
        }
        return ServerResponse.createBySuccess(map);
    }
}
