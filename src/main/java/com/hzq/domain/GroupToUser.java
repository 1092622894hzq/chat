package com.hzq.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 群，群消息和用户的关系表
 * @version: 1.0
 */
public class GroupToUser implements Serializable {

    private Integer id;
    /*
    用户主表的id
     */
    private Integer userId;
    /*
    群的id
     */
    private Integer groupId;
    /*
    群消息的已读消息的最后一个id
     */
    private Integer groupMessageId;
    /*
    用户在群内显示的昵称
     */
    private String groupNickname;
    /*
    该表的创建时间
     */
    private Timestamp gmtCreate;
    /*
    该表的修改时间
     */
    private Timestamp gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGroupMessageId() {
        return groupMessageId;
    }

    public void setGroupMessageId(Integer groupMessageId) {
        this.groupMessageId = groupMessageId;
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

    public Timestamp getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Timestamp gmtModified) {
        this.gmtModified = gmtModified;
    }
}
















