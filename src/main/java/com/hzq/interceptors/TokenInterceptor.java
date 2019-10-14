package com.hzq.interceptors;

import com.hzq.common.Const;
import com.hzq.execption.CustomGenericException;
import com.hzq.utils.JwtUil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: blue
 * @Date: 2019/9/29
 * @Description: token拦截器，验证用户是否包含正确的token
 * @version: 1.0
 */
public class TokenInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
        LOGGER.debug("到达token拦截器");
        //判断是否刷新token,是的话直接放行
        if (request.getAttribute(Const.REFRESH_TOKEN) != null) {
            return true;
        }
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("accessToken");
        LOGGER.debug("token: "+token);
        if (null == token || !JwtUil.verify(token)) {
            LOGGER.debug("发现token不正确");
            throw  CustomGenericException.CreateException(20,"用户的token无效");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e){

    }
}
