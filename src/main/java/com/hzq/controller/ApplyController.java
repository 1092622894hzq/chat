package com.hzq.controller;

import com.hzq.vo.ServerResponse;
import com.hzq.domain.Apply;
import com.hzq.service.ApplyService;
import com.hzq.vo.ApplyVo;
import com.hzq.vo.FriendVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther: blue
 * @Date: 2019/10/2
 * @Description: 好友申请
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
     * 根据申请人id删除申请
     * @param fromId 申请人id
     * @param toId 被申请人id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/deleteById/{fromId}/{toId}", method = RequestMethod.GET)
    public ServerResponse<String> deleteById(@PathVariable Integer fromId, @PathVariable Integer toId) {
        return applyService.deleteById(fromId,toId);
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
    public ServerResponse<FriendVo> update(@RequestBody Apply apply) {
        return applyService.update(apply);
    }


    /**
     * 根据用户id查询所有申请人的信息
     * @param userId 用户id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/selectAll/{userId}", method = RequestMethod.GET)
    public ServerResponse<List<ApplyVo>> selectAll(@PathVariable Integer userId) {
        return applyService.selectAll(userId);
    }

    /**
     * 根据申请人和被申请人id来查询申请人信息
     * @param fromId 申请人的id
     * @param toId 被申请人的id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/select/{fromId}/{toId}", method = RequestMethod.GET)
    public ServerResponse<ApplyVo> select(@PathVariable Integer fromId, @PathVariable Integer toId) {
        return applyService.select(fromId,toId);
    }

}
