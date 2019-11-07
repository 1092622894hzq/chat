package com.hzq.handler;

import com.hzq.vo.ServerResponse;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: 统一异常处理类
 * @version: 1.0
 */
@Component
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定以的异常
     * @param e 异常
     * @return 返回通用结果
     */
    @ExceptionHandler(CustomGenericException.class)
    @ResponseBody
    public ServerResponse<String> allExceptionHandler(CustomGenericException e){
        LOGGER.error("到达统一CustomGenericException异常处理方法");
        LOGGER.error(e.getErrMsg(),e);
        return ServerResponse.createByException(e);
    }

    /**
     * 处理所有不可知的异常
     * @param e 异常
     * @return 返回通用结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ServerResponse<String> handleException(Exception e){
        LOGGER.error("到达统一Exception异常处理方法");
        e.printStackTrace();
        LOGGER.error(e.getMessage(), e);
        return ServerResponse.createByResponseCodeEnum(ResponseCodeEnum.SYSTEM_ERROR);
    }

}
