package com.hzq.vo;

import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/11/3
 * @Description: 群用户的个人信息
 * @version: 1.0
 */
public class GroupUserVo {
    /*
    用户主表的id  groupToUser接口
     */
    private Integer userId;
    /*
    群的id
    */
    private Integer groupId;
    /*
    用户在群内显示的昵称
     */
    private String groupNickname;
    /*
    该表的创建时间
    */
    private Timestamp gmtCreate;
    /*
    用户昵称  userInfo接口
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
    用户账号   user接口
     */
    private String username;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupNickname() {
        return groupNickname;
    }

    public void setGroupNickname(String groupNickname) {
        this.groupNickname = groupNickname;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
