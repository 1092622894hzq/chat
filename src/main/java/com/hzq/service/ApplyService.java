package com.hzq.service;

import com.hzq.common.ServerResponse;
import com.hzq.domain.Apply;

import java.util.List;
import java.util.Map;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: com.hzq.service
 * @version: 1.0
 */
public interface ApplyService {

    /**
     * 添加好友申请
     * @param apply 好友申请信息
     * @return 返回通用对象
     */
    ServerResponse<String> insert(Apply apply);

    /**
     * 根据数据的id
     * @param id 数据的id
     * @return 返回通用对象
     */
    ServerResponse<String> deleteById(Integer id);

    /**
     * 根据用户id删除所有好友申请
     * @param userId 用户id
     * @return 返回通用对象
     */
    ServerResponse<String> deleteByUserId(Integer userId);

    /**
     * 根据用户id和好友id更新申请状态
     * @param apply 申请信息
     * @return 返回通用对象
     */
    ServerResponse<String> update(Apply apply);

    /**
     * 根据用户id查询所有好友申请
     * @param userId 用户id
     * @return 返回通用对象
     */
    ServerResponse<Map<Integer,List<Apply>>> selectAll(Integer userId);

}
