package com.hzq.domain;

import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.domain
 * @version: 1.0
 */
public class Message {
    private Integer id;
    private String messageContent;
    private Integer messageStatus;
    private Integer messageGroup;
    private Integer messageType;
    private Integer messageFromId;
    private Integer messageToId;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    private Integer bigIdDelete;
    private Integer smallIdDelete;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Integer getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Integer messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Integer getMessageGroup() {
        return messageGroup;
    }

    public void setMessageGroup(Integer messageGroup) {
        this.messageGroup = messageGroup;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getMessageFromId() {
        return messageFromId;
    }

    public void setMessageFromId(Integer messageFromId) {
        this.messageFromId = messageFromId;
    }

    public Integer getMessageToId() {
        return messageToId;
    }

    public void setMessageToId(Integer messageToId) {
        this.messageToId = messageToId;
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

    public Integer getBigIdDelete() {
        return bigIdDelete;
    }

    public void setBigIdDelete(Integer bigIdDelete) {
        this.bigIdDelete = bigIdDelete;
    }

    public Integer getSmallIdDelete() {
        return smallIdDelete;
    }

    public void setSmallIdDelete(Integer smallIdDelete) {
        this.smallIdDelete = smallIdDelete;
    }
}














