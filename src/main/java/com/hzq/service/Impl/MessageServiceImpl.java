package com.hzq.service.Impl;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.dao.MessageDao;
import com.hzq.domain.Message;
import com.hzq.service.MessageService;
import com.hzq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: com.hzq.service.Impl
 * @version: 1.0
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private UserService userService;

    @Override
    public ServerResponse<String> insert(Message message) {  //默认每次先插入的是接收者的信息
        if (messageDao.insert(message) == 0) {
            return ServerResponse.createByErrorMessage("插入消息失败");
        }
        message.setMessageStatus(Const.MARK_AS_READ);
        message.setUserId(message.getMessageFromId());
        if (messageDao.insert(message) == 0) {
            return ServerResponse.createByErrorMessage("插入消息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteMessageById(Integer id) {
        if (messageDao.deleteMessageById(id) == 0) {
            return ServerResponse.createByErrorMessage("删除聊天记录失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<List<Message>> queryMessageByUserIdAndFriendId(Integer id, Integer friendId) {
        if (messageDao.queryMessageByUserIdAndFriendId(id,friendId) == null) {
            return ServerResponse.createByErrorMessage("没有查询到聊天记录");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<Map<Integer, List<Message>>> queryUnreadMessageByUserId(Integer userId) {
        List<Message> messages =  messageDao.queryUnreadMessageByUserId(userId, Const.MARK_AS_UNREAD);
        if (messages == null || messages.isEmpty()) {
            return ServerResponse.createByErrorMessage("没有找到未读的消息");
        }
        Map<Integer, List<Message>>  map = userService.MessageSubgroup(messages,new Message());
        if (messages.size() == 1) {
            messageDao.updateOneMessage(messages.get(0).getId(),Const.MARK_AS_READ);
        } else {
            messageDao.update(messages.get(messages.size()-1).getId(),messages.get(0).getId(),Const.MARK_AS_READ,userId);
        }
        return ServerResponse.createBySuccess(map);
    }


}
