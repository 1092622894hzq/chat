package com.hzq.dao;

import com.hzq.domain.GroupMessageToUser;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/10/4
 * @Description: com.hzq.dao
 * @version: 1.0
 */
public interface GroupMessageToUserDao {

    int insert(@Param("messageToUser") GroupMessageToUser messageToUser);

    int update(@Param("id") Integer id, @Param("time") Timestamp time);

    int delete(@Param("userId") Integer userId);

}
