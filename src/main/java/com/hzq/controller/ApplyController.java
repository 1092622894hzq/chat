package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.domain.Apply;
import com.hzq.domain.User;
import com.hzq.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: com.hzq.controller
 * @version: 1.0
 */
@RestController
@RequestMapping("/apply")
public class ApplyController {

    @Autowired
    private ApplyService applyService;

    /**
     * 提交好友申请,利用mybatis同时执行两条插入语句
     * 必须的参数：fromId,toId,applyReason
     * @param apply 申请
     * @return 返回通用对象
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ServerResponse<String> insert(@RequestBody Apply apply) {
        return applyService.insert(apply);
    }

    /**
     * 根据申请的id删除申请
     * @param id 该数据的id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ServerResponse<String> deleteById(@PathVariable Integer id) {
        return applyService.deleteById(id);
    }


    /**
     * 根据用户id删除所有好友申请
     * @param userId 用户id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/deleteByUserId/{userId}", method = RequestMethod.GET)
    public ServerResponse<String> deleteByUserId(@PathVariable Integer userId) {
        return applyService.deleteByUserId(userId);
    }

    /**
     * 更新申请状态
     * 必须的参数：fromId,toId
     * @param apply 申请信息
     * @return 返回通用对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ServerResponse<String> update(@RequestBody Apply apply) {
        return applyService.update(apply);
    }


    /**
     * 根据用户id查询所有好友申请
     * @param userId 用户id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/selectAll/{userId}", method = RequestMethod.GET)
    public ServerResponse<Map<Integer,List<Apply>>> selectAll(@PathVariable Integer userId) {
        return applyService.selectAll(userId);
    }

}
