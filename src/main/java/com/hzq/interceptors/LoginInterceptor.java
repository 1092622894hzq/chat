package com.hzq.interceptors;

import com.hzq.common.Const;
import com.hzq.domain.User;
import com.hzq.execption.CustomGenericException;
import com.hzq.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: blue
 * @Date: 2019/10/6
 * @Description: 登录访问之前拦截，判断是否已经登录了
 * @version: 1.0
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //去数据库，查看标志位是否登录
//        String username = httpServletRequest.getParameter(Const.USERNAME);
//        User user = userService.selectByUsername(username).getData();
//        if (Const.ONLINE.equals(user.getStatus())) {
//            LOGGER.debug("用户已经登陆了");
//            throw CustomGenericException.CreateException(-20,"用户已经登陆了");
//        }
        if (httpServletRequest.getSession().getAttribute(Const.CURRENT_USER) != null) {
            LOGGER.debug("用户已经登陆了");
            throw CustomGenericException.CreateException(-20,"用户已经登陆了");
        }
        return true;
    }

}
