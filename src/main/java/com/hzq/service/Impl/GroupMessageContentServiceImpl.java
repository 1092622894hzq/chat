package com.hzq.service.Impl;

import com.hzq.service.GroupToUserService;
import com.hzq.vo.ServerResponse;
import com.hzq.dao.GroupMessageContentDao;
import com.hzq.domain.GroupMessageContent;
import com.hzq.domain.GroupToUser;
import com.hzq.service.GroupMessageContentService;
import com.hzq.vo.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
    private GroupToUserService groupToUserService;
    @Autowired
    private GroupMessageContentDao messageContentDao;

    @Override
    public ServerResponse insert(SendMessage message) {
        if (messageContentDao.insert(message) == 0) {
            return ServerResponse.createByErrorMessage("存放群聊消息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<List<SendMessage>> selectAll(Integer groupId,Integer userId) {
        List<SendMessage> groupMessageContents = messageContentDao.selectAll(groupId,userId);
        if (groupMessageContents == null) {
            return ServerResponse.createByErrorMessage("没有查询到群聊记录");
        }
        return ServerResponse.createBySuccess(groupMessageContents);
    }

    @Override
    public ServerResponse delete(Integer id,Integer groupId,Integer userId) {
        if (messageContentDao.insertToDelete(id,groupId,userId) == 0) {
            return ServerResponse.createByErrorMessage("删除群聊消息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<List<GroupMessageContent>> selectUnread(Integer userId, Integer groupId) {
        List<GroupMessageContent> messageContents = messageContentDao.selectUnread(userId,groupId);
        if (messageContents == null) {
            return ServerResponse.createBySuccessMessage("查询不到未读消息");
        }
        return ServerResponse.createBySuccess(messageContents);
    }

    @Override
    public ServerResponse<List<SendMessage>> selectAllUnread(Integer userId) {
        List<SendMessage> messageContents = messageContentDao.selectAllUnread(userId);
        if (messageContents == null) {
            return ServerResponse.createByErrorMessage("查询不到未读消息");
        }
        return ServerResponse.createBySuccess(messageContents);
}

    @Override
    public ServerResponse update(List<SendMessage> messageContents, Integer userId) {
        Map<Integer,List<SendMessage>> map = MessageSubgroup(messageContents);
        if (map != null) {
            for (Integer key : map.keySet()) {
                //最新的未读的消息的id
                Integer messageId = map.get(key).get(map.get(key).size()-1).getId();
                Integer groupId = map.get(key).get(map.get(key).size()-1).getToIdOrGroupId();
                GroupToUser groupToUser = new GroupToUser();
                groupToUser.setUserId(userId);
                groupToUser.setGroupId(groupId);
                groupToUser.setGroupMessageId(messageId);
                groupToUserService.update(groupToUser);
            }
        }
        return ServerResponse.createBySuccess();
    }
    private Map<Integer,List<SendMessage>> MessageSubgroup(List<SendMessage> messages) {
        if (messages == null) return null;
        List<SendMessage> listContent;
        Map<Integer,List<SendMessage>> map = new HashMap<>();
        for (SendMessage message : messages) {
            Integer key = message.getToIdOrGroupId();
            if (map.get(key) != null) {
                map.get(key).add(message);
            } else {
                listContent = new ArrayList<>();
                listContent.add(message);
                map.put(key,listContent);
            }
        }
        return map;
    }
}