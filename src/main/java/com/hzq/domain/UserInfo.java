package com.hzq.domain;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 用户个人信息
 * @version: 1.0
 */
public class UserInfo implements Serializable {
    /*
    用户昵称
     */
    private String nickname;
    /*
    用户头像
     */
    private String avatar;
    /*
    用户性别
     */
    private Integer sex;
    /*
    用户邮箱
     */
    private String email;
    /*
    用户个性签名
     */
    private String sign;
    /*
    用户主表的id 即user类中对应的id
     */
    private Integer userId;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


}





















