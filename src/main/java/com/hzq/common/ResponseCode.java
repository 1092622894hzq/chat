package com.hzq.common;

/**
 * @Auther: blue
 * @Date: 2019/9/16
 * @Description: com.hzq.controller
 * @version: 1.0
 */
public enum ResponseCode {

    SUCCESS(20,"SUCCESS"),
    MESSAGE_FORMAT_ERROR(38,"消息格式有误"),
    SEND_MESSAGE_ERROR(39,"发送消息错误"),
    ERROR(40,"ERROR"),
    NO_AUTHOR(42,"不具备权限"),
    UPLOAD_FILE_ERROR(43,"上传文件出错"),
    SERVER_ERROR(44,"服务器内部错误");


    private final int code;
    private final String desc;


    ResponseCode(int code,String desc){
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
