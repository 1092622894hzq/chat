package com.hzq.domain;


import com.google.gson.Gson;

import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.domain
 * @version: 1.0
 */
public class GroupMessageContent {
    private Integer id;
    private String gmContent;
    private Integer gmType;
    private Integer gmFromId;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
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

}












