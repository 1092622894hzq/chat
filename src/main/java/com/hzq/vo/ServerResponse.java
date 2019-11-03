package com.hzq.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hzq.enums.ResponseCodeEnum;
import com.hzq.execption.CustomGenericException;

import java.io.Serializable;

/**
 * @Auther: blue
 * @Date: 2019/9/16
 * @Description: com.hzq.controller
 * @version: 1.0
 */
@JsonSerialize(include =  JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候,如果是null的对象,key也会消失
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private String accessToken;
    private String sessionId;
    private T data;

    private ServerResponse(int status){
        this.status = status;
    }
    private ServerResponse(int status, T data){
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg){
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

   @JsonIgnore//使之不在json序列化结果当中
    public boolean isSuccess(){
        return this.status == ResponseCodeEnum.SUCCESS.getCode();
    }

    public int getStatus(){
        return status;
    }
    public String getMsg(){
        return msg;
    }
    public T getData(){
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public String getAccessToken(){
        return accessToken;
    }


    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<>(ResponseCodeEnum.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<>(ResponseCodeEnum.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<>(ResponseCodeEnum.SUCCESS.getCode(),data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<>(ResponseCodeEnum.SUCCESS.getCode(),msg,data);
    }


    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<>(ResponseCodeEnum.ERROR.getCode(), ResponseCodeEnum.ERROR.getDesc());
    }


    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<>(ResponseCodeEnum.USER_ERROR.getCode(),errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(Integer errorCode,String errorMessage){
        return new ServerResponse<>(errorCode,errorMessage);
    }

    public static <T> ServerResponse<T> createByResponseCodeEnum(ResponseCodeEnum responseCodeEnum) {
        return new ServerResponse<>(responseCodeEnum.getCode(),responseCodeEnum.getDesc());
    }

    public static <T> ServerResponse<T> createByException(CustomGenericException e) {
        return new ServerResponse<>(e.getErrCode(),e.getErrMsg());
    }

}
