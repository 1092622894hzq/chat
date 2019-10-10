package com.hzq.service.Impl;


import com.hzq.common.ServerResponse;
import com.hzq.dao.ApplyDao;

import com.hzq.domain.Apply;

import com.hzq.service.ApplyService;
import com.hzq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: com.hzq.service.Impl
 * @version: 1.0
 */
@Service("applyService")
public class ApplyServiceImpl implements ApplyService {

    @Autowired
    private ApplyDao applyDao;
    @Autowired
    private UserService userService;

    @Override
    public ServerResponse<String> insert(Apply apply) {
        if (applyDao.insert(apply) == 0) {
            return ServerResponse.createByErrorMessage("插入申请失败");
        }
        if (apply.getFromId().intValue() ==  apply.getUserId().intValue()){
            apply.setUserId(apply.getToId());
        } else {
            apply.setUserId(apply.getFromId());
        }
        if (applyDao.insert(apply) == 0) {
            return ServerResponse.createByErrorMessage("插入申请失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> delete(Apply apply) {
        if (applyDao.delete(apply) == 0) {
            return ServerResponse.createByErrorMessage("删除申请失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> deleteById(Integer userId) {
        if (applyDao.deleteById(userId) == 0) {
            return ServerResponse.createBySuccessMessage("没有申请可修改");
        }
        return ServerResponse.createBySuccess();
    }


    @Override
    public ServerResponse<String> update(Apply apply) {
        if (applyDao.update(apply) == 0) {
            return ServerResponse.createByErrorMessage("更新申请失败");
        }
        Integer fromId = apply.getFromId();
        apply.setFromId(apply.getToId());
        apply.setToId(fromId);
        if (applyDao.update(apply) == 0) {
            return ServerResponse.createByErrorMessage("更新申请失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<Map<Integer,List<Apply>>> selectAll(Integer id) {
        List<Apply> applies = applyDao.selectAll(id);
        if ( applies == null) {
            return ServerResponse.createByErrorMessage("查询不到好友申请");
        }
        Map<Integer,List<Apply>> map = userService.MessageSubgroup(applies,new Apply());
        return ServerResponse.createBySuccess(map);
    }
}
