package com.hzq.service;


import com.hzq.vo.ServerResponse;
import com.hzq.domain.Group;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: com.hzq.service
 * @version: 1.0
 */
public interface GroupService {

    /**
     * 创建群聊，同时将群主的信息插入到群聊成员对应的表中
     * @param group 群的信息
     */
    void insert(Group group);

    /**
     * 根据群id删除群
     * @param id 群id
     * @return 返回通用对象
     */
    ServerResponse<String> delete(Integer id);

    /**
     * 更新群聊信息
     * @param group 群的相关信息
     */
    void update(Group group);

    /**
     * 根距用户id查询所有群
     * @param userId 用户userId
     * @return 返回存有所有群的通用对象
     */
    ServerResponse<List<Group>> selectAll(Integer userId);

    /**
     * 根据群id查询群信息
     * @param id 群id
     * @return 返回群信息通用对象
     */
    ServerResponse<Group> select(Integer id);


}
