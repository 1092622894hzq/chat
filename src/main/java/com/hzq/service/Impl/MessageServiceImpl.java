package com.hzq.service.Impl;

import com.hzq.common.Const;
import com.hzq.vo.ServerResponse;
import com.hzq.dao.MessageDao;
import com.hzq.service.MessageService;
import com.hzq.vo.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: 私聊消息
 * @version: 1.0
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Override
    public ServerResponse<String> insert(SendMessage message, Integer messageStatus, Integer userId) {  //默认每次先插入的是接收者的信息
        if (messageDao.insert(message,messageStatus,userId) == 0) {
            return ServerResponse.createByErrorMessage("插入消息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public void update(List<SendMessage> messageList) {
        if (messageList.size() == 1) {
            if (messageDao.updateOneMessage(messageList.get(0).getId(),Const.MARK_AS_READ) == 0) {
                System.out.println("更新消息状态失败");
            }
        } else {
            Integer bigId = messageList.get(messageList.size()-1).getId();
            Integer smallId = messageList.get(0).getId();
            Integer id = messageList.get(0).getToIdOrGroupId();
            if (messageDao.update(bigId,smallId,Const.MARK_AS_READ,id) == 0) {
                System.out.println("更新消息状态失败");
            }
        }
    }

    @Override
    public ServerResponse<String> deleteMessageById(Integer id) {
        messageDao.deleteMessageById(id);
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteMessageByUserIdAndFriendId(Integer userId, Integer friendId) {
        messageDao.deleteMessageByUserIdAndFriendId(userId,friendId);
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<List<SendMessage>> queryMessageByUserIdAndFriendId(Integer id, Integer friendId) {
        List<SendMessage> list = messageDao.queryMessageByUserIdAndFriendId(id,friendId);
        if (list == null) {
            return ServerResponse.createByErrorMessage("没有查询到聊天记录");
        }
        return ServerResponse.createBySuccess(list);
    }

    @Override
    public ServerResponse<List<SendMessage>> selectUnReadSendMessage(Integer userId) {
        List<SendMessage> messages =  messageDao.selectUnReadSendMessage(userId, Const.MARK_AS_UNREAD);
        if (messages == null || messages.isEmpty()) {
            return ServerResponse.createByErrorMessage("没有找到未读的消息");
        }
        return ServerResponse.createBySuccess(messages);
    }


}
