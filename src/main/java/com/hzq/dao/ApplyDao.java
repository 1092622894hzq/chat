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
     * 修改删除标志
     * @param apply 修改的申请
     * @return 返回修改次数
     */
    int delete(Apply apply);

    /**
     * 更新申请状态
     * @param apply 修改的申请
     * @return 返回修改次数
     */
    int update(Apply apply);

    /**
     * 删除申请信息
     * @param delete 删除的状态
     * @return 返回修改次数
     */
    int bothDelete(Integer delete);

    /**
     * 根据用户id查询所有用户
     * @param id 用户id
     * @return 返回申请集合
     */
    List<Apply> selectAll(@Param("id") Integer id);

}
