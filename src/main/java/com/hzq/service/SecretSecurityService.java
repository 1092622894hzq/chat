package com.hzq.service;

import com.hzq.vo.ServerResponse;
import com.hzq.domain.SecretSecurity;

/**
 * @Auther: blue
 * @Date: 2019/11/1
 * @Description: com.hzq.service
 * @version: 1.0
 */
public interface SecretSecurityService {

    /**
     * 插入密保问题
     * @param security 存储密保问题的对象
     * @return 返回通用对象
     */
    ServerResponse<String> insert(SecretSecurity security);

    /**
     * 查询密保问题和答案
     * @param userId 用户id
     * @return 返回通用对象
     */
    ServerResponse<SecretSecurity> select(Integer userId);

    /**
     * 更新密保问题和答案
     * @param security 存储要更新的数据
     * @return 返回通用对象
     */
    ServerResponse<String> update(SecretSecurity security);

    /**
     * 验证密保问题和答案
     * @param security 密保
     * @return 返回通用对象
     */
    ServerResponse<String> verify(SecretSecurity security);
}
