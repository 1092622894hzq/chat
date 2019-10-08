package com.hzq.service;

import com.hzq.common.ServerResponse;
import com.hzq.domain.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: com.hzq.service
 * @version: 1.0
 */
public interface MessageService {

    ServerResponse<String> insert(Message message);

    ServerResponse<String> deleteMessageByUserId(Message message);

    ServerResponse<List<Message>> queryMessageByUserIdAndFriendId(Integer id, Integer friendId);

    ServerResponse<List<Message>> queryUnreadMessageByUserId(Integer id);
}
