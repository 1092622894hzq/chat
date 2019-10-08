package com.hzq.common;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @Auther: blue
 * @Date: 2019/9/13
 * @Description: com.hzq.domain
 * @version: 1.0
 */
@Component
@ControllerAdvice
public class CustomGenericException extends RuntimeException  {
    private Integer errCode;
    private String errMsg;

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public CustomGenericException(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public CustomGenericException(){

    }
}
