package com.hzq.service;

import com.hzq.common.ServerResponse;
import com.hzq.domain.GroupMessageToUser;

import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/10/4
 * @Description: com.hzq.service
 * @version: 1.0
 */
public interface GroupMessageToUserService {

    ServerResponse<String> insert(GroupMessageToUser messageToUser);

    ServerResponse<String> update(Integer id, Timestamp time);

    ServerResponse<String> delete(Integer userId);


}
