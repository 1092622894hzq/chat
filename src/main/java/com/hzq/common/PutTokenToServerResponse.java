package com.hzq.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: blue
 * @Date: 2019/10/5
 * @Description: com.hzq.common
 * @version: 1.0
 */
@ControllerAdvice
public class PutTokenToServerResponse implements ResponseBodyAdvice<ServerResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PutTokenToServerResponse.class);

    //这里是一个前置拦截匹配操作,其实就是告诉你满足为true的才会执行下面的beforeBodyRead方法,这里可以定义自己业务相关的拦截匹配
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * 此方法是拦截返回值，并且操作返回值
     * @param serverResponse 自己写的通用返回值对象
     * @param methodParameter 方法参数
     * @param mediaType 不知道
     * @param aClass 不知道
     * @param serverHttpRequest 请求
     * @param serverHttpResponse 响应
     * @return 返回通用对象
     */
    @Override
    public ServerResponse beforeBodyWrite(ServerResponse serverResponse, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        LOGGER.debug("进入返回值对象处理器中");
        HttpServletRequest request = ((ServletServerHttpRequest)serverHttpRequest).getServletRequest();
        String accessToken = request.getHeader(Const.ACCESS_TOKEN);
        if (accessToken != null) {
            serverResponse.setAccessToken(accessToken);
        }
        return serverResponse;
    }
}
