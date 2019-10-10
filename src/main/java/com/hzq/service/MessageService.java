package com.hzq.service;

import com.hzq.common.ServerResponse;
import com.hzq.domain.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: com.hzq.service
 * @version: 1.0
 */
public interface MessageService {

    ServerResponse<String> insert(Message message);

    ServerResponse<String> deleteMessageById(Integer id);

    ServerResponse<List<Message>> queryMessageByUserIdAndFriendId(Integer id, Integer friendId);


    /**
     * 根据用户id查询所有私聊未读消息
     * @param userId 用户主表id
     * @return 返回未读消息集合
     */
    ServerResponse<Map<Integer,List<Message>>> queryUnreadMessageByUserId(@Param("userId") Integer userId);
}
