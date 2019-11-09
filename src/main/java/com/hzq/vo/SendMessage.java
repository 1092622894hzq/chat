package com.hzq.vo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/10/23
 * @Description: 发送聊天时的承载类
 * @version: 1.0
 */
public class SendMessage implements Serializable {
    /*
    消息的id
     */
    private Integer id;
    /*
   消息内容
    */
    private String message;
    /*
    消息内容类型 0 文字 1 文件 2 图片
     */
    private Integer messageType;
    /*
    消息类型 0 私聊 1 群聊
     */
    private Integer type;
    /*
    发送者id
     */
    private Integer fromId;
    /*
    接收者id/群id
     */
    private Integer toIdOrGroupId;
    /*
    发送的时间
     */
    private Timestamp gmtCreate;
    /*
    发送者头像
     */
    private String avatar;
    /*
    私聊：发送者个人信息的昵称
    群聊：发送者在群内昵称
     */
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToIdOrGroupId() {
        return toIdOrGroupId;
    }

    public void setToIdOrGroupId(Integer toIdOrGroupId) {
        this.toIdOrGroupId = toIdOrGroupId;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SendMessage(){}

    public SendMessage(Integer type, String message){
        this.type = type;
        this.message = message;
    }


    @Override
    public String toString() {
        return "SendMessage{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", messageType=" + messageType +
                ", type=" + type +
                ", fromId=" + fromId +
                ", toIdOrGroupId=" + toIdOrGroupId +
                ", gmtCreate=" + gmtCreate +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
