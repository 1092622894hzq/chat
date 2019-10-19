package com.hzq.vo;

import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/10/19
 * @Description: 申请的vo
 * @version: 1.0
 */
public class ApplyVo {
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
    申请状态 0 未接收,未处理 1 同意 -1 拒绝
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

    /*
    申请人的账号
     */
    private String username;

    /*
    申请人的头像
     */
    private String avatar;
    /*
    申请人的性别 性别 1 表示男 0 表示女
     */
    private Integer sex;
    /*
    申请人的个性签名
     */
    private String sign;
    /*
    申请人的昵称
     */
    private String nickname;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
