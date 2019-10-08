package com.hzq.service;

import com.hzq.common.ServerResponse;
import com.hzq.domain.Apply;
import com.hzq.domain.UserInfo;

import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: com.hzq.service
 * @version: 1.0
 */
public interface ApplyService {

    ServerResponse<String> insert(Apply apply);

    ServerResponse<String> delete(Apply apply);

    ServerResponse<String> update(Apply apply);

    ServerResponse<List<Apply>> selectAll(Integer id);

}
