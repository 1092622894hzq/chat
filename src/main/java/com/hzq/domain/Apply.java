package com.hzq.domain;

import com.google.gson.Gson;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 申请
 * @version: 1.0
 */
public class Apply implements Serializable {
    /*
    申请id
     */
    private Integer id;
    /*
    发送者id
     */
    private Integer fromId;
    /*
    接收者id
     */
    private Integer toId;
    /*
    申请理由
     */
    private String applyReason;
    /*
    申请状态
     */
    private Integer applyStatus;
    /*
    申请创建时间
     */
    private Timestamp gmtCreate;
    /*
    申请修改时间
     */
    private Timestamp gmtModified;
    /*
    该申请属于哪个用户的id
     */
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Timestamp getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Timestamp gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String toJson(){
        return gson.toJson(this);
    }

    private static Gson gson = new Gson();

    @Override
    public String toString() {
        return "Apply{" +
                "id=" + id +
                ", fromId=" + fromId +
                ", toId=" + toId +
                ", applyReason='" + applyReason + '\'' +
                ", applyStatus=" + applyStatus +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", userId=" + userId +
                '}';
    }
}
