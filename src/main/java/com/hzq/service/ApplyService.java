package com.hzq.service;

import com.hzq.vo.ServerResponse;
import com.hzq.domain.Apply;
import com.hzq.vo.ApplyVo;
import com.hzq.vo.FriendVo;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: 申请
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
     * 删除特定申请
     * @param fromId 申请人id
     * @param toId 被申请人id
     * @return 返回通用对象
     */
    ServerResponse<String> delete(Integer fromId, Integer toId, HttpSession session);

    /**
     * 根据申请的id删除申请
     * @param fromId 申请人id
     * @param toId 被申请人id
     * @return 返回通用对象
     */
    ServerResponse<String> deleteById(Integer fromId , Integer toId);

    /**
     * 根据用户id删除所有跟用户相关的好友申请
     * @param userId 用户id
     * @return 返回通用对象
     */
    ServerResponse<String> deleteByUserId(Integer userId);

    /**
     * 根据申请人id和被申请人id来更新申请状态
     * @param apply 申请信息
     * @return 返回通用对象
     */
    ServerResponse<FriendVo> update(Apply apply);

    /**
     * 根据用户id查询所有好友申请
     * @param userId 用户id
     * @return 返回通用对象
     */
    ServerResponse<List<ApplyVo>> selectAll(Integer userId);

    /**
     * 根据申请人id和被申请人id查询申请人信息
     * @param fromId 申请人id
     * @param toId 被申请人id
     * @return 通用对象
     */
    ServerResponse<ApplyVo> select(Integer fromId, Integer toId);

    /**
     * 根据申请人id和被申请人id查询申请人信息
     * @param fromId 申请人id
     * @param toId 被申请人id
     * @return 返回个数
     */
    int checkApply(Integer fromId, Integer toId);
}
