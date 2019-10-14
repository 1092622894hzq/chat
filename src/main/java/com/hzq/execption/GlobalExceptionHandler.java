package com.hzq.execption;

import com.hzq.common.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: com.hzq.common
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
        e.printStackTrace();
        LOGGER.error(e.getErrMsg(),e);
        return ServerResponse.createByErrorCodeMessage(e.getErrCode(), e.getErrMsg());
    }

    /**
     * 处理所有不可知的异常
     * @param e 异常
     * @return 返回通用结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ServerResponse<String> handleException(Exception e){
        e.printStackTrace();
        LOGGER.error(e.getMessage(), e);
        return ServerResponse.createByErrorMessage("操作错误");
    }

}
