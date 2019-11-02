package com.hzq.dao;

import com.hzq.domain.SecretSecurity;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: blue
 * @Date: 2019/11/1
 * @Description: 密保dao
 * @version: 1.0
 */
public interface SecretSecurityDao {

    /**
     * 插入密保问题
     * @param security 存储密保问题的对象
     * @return 返回次数
     */
    int insert(SecretSecurity security);

    /**
     * 查询密保问题和答案
     * @param userId 用户id
     * @return 返回对象
     */
    SecretSecurity select(@Param("userId") Integer userId);

    /**
     * 更新密保问题和答案
     * @param security 存储要更新的数据
     * @return 返回修改次数
     */
    int update(SecretSecurity security);

    /**
     * 查询设置了密保问题没
     * @param userId 用户id
     * @return 返回修该次数
     */
    int check(@Param("userId") Integer userId);
}
