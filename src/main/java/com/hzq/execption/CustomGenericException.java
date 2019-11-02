package com.hzq.execption;

import com.hzq.enums.ResponseCodeEnum;
import org.springframework.stereotype.Component;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 自定义统一异常类
 * @version: 1.0
 */
@Component
public class CustomGenericException extends RuntimeException  {

    //异常数字
    private Integer errCode;
    //异常信息
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

    private CustomGenericException() {
    }

    private CustomGenericException(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

     private CustomGenericException(ResponseCodeEnum responseCodeEnum) {
        this.errCode = responseCodeEnum.getCode();
        this.errMsg = responseCodeEnum.getDesc();
    }

    public static CustomGenericException CreateException(Integer errCode, String errMsg) {
        return new CustomGenericException(errCode,errMsg);
    }

    public static CustomGenericException CreateException(ResponseCodeEnum responseCodeEnum) {
        return new CustomGenericException(responseCodeEnum);
    }

}
