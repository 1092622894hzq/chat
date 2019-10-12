package com.hzq.controller;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import com.hzq.domain.Apply;
import com.hzq.domain.User;
import com.hzq.service.ApplyService;
import org.apache.ibatis.annotations.Param;
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
     * 提交好友申请
     * @param apply 申请
     * @return 返回通用对象
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ServerResponse<String> insert(@RequestBody Apply apply, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        apply.setUserId(user.getId());
        return applyService.insert(apply);
    }

    /**
     * 根据用户id和申请好友的id删除申请
     * @param apply 申请信息
     * @param session 一次会话
     * @return 返回通用对象
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ServerResponse<String> delete(@RequestBody Apply apply, HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        apply.setUserId(user.getId());
        return applyService.delete(apply);
    }

    /**
     * 根据用户id删除所有好友申请
     * @param userId 用户id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.GET)
    public ServerResponse<String> deleteById(@PathVariable Integer userId) {
        return applyService.deleteById(userId);
    }

    /**
     * 更新申请
     * @param apply 申请信息
     * @return 返回通用对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ServerResponse<String> update(@RequestBody Apply apply) {
        return applyService.update(apply);
    }


    /**
     * 根据用户id查询所有好友申请
     * @param session 一次会话
     * @return 返回通用对象
     */
    @RequestMapping(value = "/selectAll")
    public ServerResponse<Map<Integer,List<Apply>>> selectAll(HttpSession session) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return applyService.selectAll(user.getId());
    }

}
