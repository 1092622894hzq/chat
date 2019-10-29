package com.hzq.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/9/28
 * @Description: 用户主表
 * @version: 1.0
 */
public class User implements Serializable {
    /*
    用户id
     */
    private Integer id;
    /*
    用户账号
     */
    private String username;
    /*
    用户密码
     */
    private String password;
    /*
    用户状态  0 下线 1 在线  2 隐身
     */
    private Integer status;
    /*
    用户登录时的时间(用户在线和隐身时就是输入账号密码时的时间，下线就是退出时的时间)
     */
    private Timestamp loginTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", loginTime=" + loginTime +
                '}';
    }
}


























