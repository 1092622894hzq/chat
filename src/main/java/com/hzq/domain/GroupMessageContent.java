package com.hzq.domain;


import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 群消息
 * @version: 1.0
 */
public class GroupMessageContent {
    /*
    消息id
     */
    private Integer id;
    /*
    消息内容--必须有
     */
    private String gmContent;
    /*
    消息类型--必须有
     */
    private Integer gmType;
    /*
    发送者--必须有
     */
    private Integer gmFromId;
   /*
   创建时间--必须有
    */
    private Timestamp gmtCreate;
    /*
    修改时间--必须有
     */
    private Timestamp gmtModified;
    /*
    群id--必须有
     */
    private Integer groupId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGmContent() {
        return gmContent;
    }

    public void setGmContent(String gmContent) {
        this.gmContent = gmContent;
    }

    public Integer getGmType() {
        return gmType;
    }

    public void setGmType(Integer gmType) {
        this.gmType = gmType;
    }

    public Integer getGmFromId() {
        return gmFromId;
    }

    public void setGmFromId(Integer gmFromId) {
        this.gmFromId = gmFromId;
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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "GroupMessageContent{" +
                "gmContent='" + gmContent + '\'' +
                ", gmType=" + gmType +
                ", gmFromId=" + gmFromId +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", groupId=" + groupId +
                '}';
    }
}












