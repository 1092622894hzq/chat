package com.hzq.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 私聊消息表
 * @version: 1.0
 */
public class Message implements Serializable {
    /*
    消息的id
     */
    private Integer id;
    /*
    消息内容---必须有
     */
    private String messageContent;
    /*
    消息状态
     */
    private Integer messageStatus;
    /*
    消息类型---必须有
     */
    private Integer messageType;
    /*
    发送者id---必须有
     */
    private Integer messageFromId;
    /*
    接收者id---必须有
     */
    private Integer messageToId;
    /*
    发送的时间--必须有
     */
    private Timestamp gmtCreate;
    /*
    修改的时间--必须有
     */
    private Timestamp gmtModified;
    /*
    拥有该消息的用户id
     */
    private Integer userId;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageContent='" + messageContent + '\'' +
                ", messageStatus=" + messageStatus +
                ", messageType=" + messageType +
                ", messageFromId=" + messageFromId +
                ", messageToId=" + messageToId +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                '}';
    }
}














