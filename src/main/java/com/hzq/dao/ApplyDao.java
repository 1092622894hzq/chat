package com.hzq.dao;

import com.hzq.domain.Apply;
import com.hzq.vo.ApplyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: com.hzq.dao
 * @version: 1.0
 */
public interface ApplyDao {

    /**
     * 添加申请
     * @param apply 申请
     * @return 返回修改次数
     */
    int insert(Apply apply);

    /**
     * 根据申请的id删除申请
     * @param id 修改的申请
     * @return 返回修改次数
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 根据用户id删除所有好友申请
     * @param useId 用户id
     * @return 返回修改次数
     */
    int deleteByUserId(@Param("userId") Integer useId);

    /**
     * 根据申请人id和被申请人id来更新申请状态
     * @param apply 修改的申请
     * @return 返回修改次数
     */
    int update(Apply apply);


    /**
     * 根据用户id查询所有用户
     * @param userId 用户id
     * @return 返回申请集合
     */
    List<ApplyVo> selectAll(@Param("userId") Integer userId);

    /**
     * 根据申请人id和被申请人id查询申请人信息
     * @param fromId 申请人id
     * @param toId 被申请人id
     * @return 信息对象
     */
    ApplyVo select(@Param("fromId") Integer fromId, @Param("toId") Integer toId);
}
