package com.hzq.dao;

import com.hzq.domain.Apply;
import com.hzq.domain.UserInfo;
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
     * 删除
     * @param apply 修改的申请
     * @return 返回修改次数
     */
    int delete(Apply apply);

    /**
     * 根据用户id删除所有好友申请
     * @param useId 用户id
     * @return 返回修改次数
     */
    int deleteById(Integer useId);

    /**
     * 更新申请状态
     * @param apply 修改的申请
     * @return 返回修改次数
     */
    int update(Apply apply);


    /**
     * 根据用户id查询所有用户
     * @param userId 用户id
     * @return 返回申请集合
     */
    List<Apply> selectAll(@Param("userId") Integer userId);

}
