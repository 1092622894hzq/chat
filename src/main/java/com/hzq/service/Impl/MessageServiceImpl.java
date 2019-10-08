package com.hzq.service.Impl;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.dao.MessageDao;
import com.hzq.domain.Message;
import com.hzq.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public ServerResponse<String> insert(Message message) {
        if (messageDao.insert(message) == 0) {
            return ServerResponse.createByErrorMessage("插入消息失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteMessageByUserId(Message message) {
        if (messageDao.deleteMessageByUserId(message) == 0) {
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
    public ServerResponse<List<Message>> queryUnreadMessageByUserId(Integer id) {
        List<Message> messages =  messageDao.queryUnreadMessageByUserId(id, Const.MARK_AS_UNREAD);
        if (messages == null) {
            return ServerResponse.createByErrorMessage("没有找到未读的消息");
        }
        if (messages.size() == 1) {
            messageDao.updateOneMessage(messages.get(0).getId(),Const.MARK_AS_READ);
        } else {
            messageDao.update(messages.get(messages.size()-1).getId(),messages.get(0).getId(),Const.MARK_AS_READ);
        }
        return ServerResponse.createBySuccess(messages);
    }
}
