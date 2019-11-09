package com.hzq.controller;

import com.hzq.vo.ServerResponse;
import com.hzq.domain.SecretSecurity;
import com.hzq.service.SecretSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: blue
 * @Date: 2019/11/1
 * @Description: com.hzq.controller
 * @version: 1.0
 */
@RestController
@RequestMapping("/security")
public class SecretSecurityController {

    @Autowired
    private SecretSecurityService secretSecurityService;


    /**
     * 添加密保
     * @param security 密保
     * @return 返回通用对象
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ServerResponse insert(@RequestBody SecretSecurity security) {
        return secretSecurityService.insert(security);
    }

    /**
     * 查询密保
     * @param userId 用户id
     * @return 返回通用对象
     */
    @RequestMapping(value = "/select/{userId}", method = RequestMethod.GET)
    public ServerResponse select(@PathVariable Integer userId) {
        return secretSecurityService.select(userId);
    }

    /**
     * 更新密保
     * @param security 密保
     * @return 返回通用对象
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ServerResponse update(@RequestBody SecretSecurity security) {
        return secretSecurityService.update(security);
    }

    /**
     * 验证密保答案
     * @param security 密保答案
     * @return 返回通用对象
     */
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public ServerResponse verify(@RequestBody SecretSecurity security) {
        return secretSecurityService.verify(security);
    }

}
