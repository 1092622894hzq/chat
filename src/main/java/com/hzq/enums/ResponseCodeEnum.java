package com.hzq.enums;

/**
 * @Auther: blue
 * @Date: 2019/9/16
 * @Description: com.hzq.controller
 * @version: 1.0
 */
public enum ResponseCodeEnum {

    SYSTEM_ERROR(-20,"系统错误"),
    SUCCESS(20,"SUCCESS"),
    ERROR(40,"ERROR"),
    USER_ERROR(40,"用户错误");

    private final int code;
    private final String desc;


    ResponseCodeEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }
}
