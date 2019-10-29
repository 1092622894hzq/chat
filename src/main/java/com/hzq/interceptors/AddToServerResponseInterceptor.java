package com.hzq.interceptors;

import com.hzq.common.Const;
import com.hzq.common.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: blue
 * @Date: 2019/10/5
 * @Description: 将token和sessionId添加到方法的返回值上
 * @version: 1.0
 */
@ControllerAdvice
public class AddToServerResponseInterceptor implements ResponseBodyAdvice<ServerResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddToServerResponseInterceptor.class);

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
        LOGGER.debug("进入返回值对象处理器中，将token和sessionId返回给安卓");
        HttpServletRequest request = ((ServletServerHttpRequest)serverHttpRequest).getServletRequest();
        HttpServletResponse response =((ServletServerHttpResponse)serverHttpResponse).getServletResponse();
        //设置允许跨域访问
        response.setHeader("Access-Control-Allow-Origin","*");
        String accessToken = request.getHeader(Const.ACCESS_TOKEN);
        String sessionId = request.getSession().getId();
        if (accessToken != null) {
            serverResponse.setAccessToken(accessToken);
            serverResponse.setSessionId("JSESSIONID="+sessionId);
        }
        return serverResponse;
    }
}
