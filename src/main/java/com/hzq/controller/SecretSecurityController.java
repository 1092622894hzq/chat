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

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ServerResponse<String> insert(@RequestBody SecretSecurity security) {
        return secretSecurityService.insert(security);
    }

    @RequestMapping(value = "/select/{userId}", method = RequestMethod.GET)
    public ServerResponse<SecretSecurity> select(@PathVariable Integer userId) {
        return secretSecurityService.select(userId);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ServerResponse<String> update(@RequestBody SecretSecurity security) {
        return secretSecurityService.update(security);
    }

}
