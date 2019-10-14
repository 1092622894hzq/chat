package com.hzq.vo;

import javax.security.auth.Subject;
import java.security.Principal;

/**
 * @Auther: blue
 * @Date: 2019/10/14
 * @Description: 聊天认证类
 * @version: 1.0
 */
public class WebsocketUserVo implements Principal {
    private String id;

    @Override
    public String getName() {
        return id;
    }

    public WebsocketUserVo(String id) {
        this.id = id;
    }


}
