package com.hzq.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.domain
 * @version: 1.0
 */
public class Group implements Serializable {
    /*
    群id
     */
    private Integer id;
    /*
    群名字
     */
    private String groupName;
    /*
    群主id
     */
    private Integer groupAdminId;
    /*
    群头像
     */
    private String groupIcon;
    /*
    群通知
     */
    private String groupNotice;
    /*
    群公告
     */
    private String groupIntroduction;
    /*
    群创建时间
     */
    private Timestamp gmtCreate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupAdminId() {
        return groupAdminId;
    }

    public void setGroupAdminId(Integer groupAdminId) {
        this.groupAdminId = groupAdminId;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getGroupNotice() {
        return groupNotice;
    }

    public void setGroupNotice(String groupNotice) {
        this.groupNotice = groupNotice;
    }

    public String getGroupIntroduction() {
        return groupIntroduction;
    }

    public void setGroupIntroduction(String groupIntroduction) {
        this.groupIntroduction = groupIntroduction;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Group(){
    }

    public Group(Integer id,Integer groupAdminId){
        this.id = id;
        this.groupAdminId = groupAdminId;
    }

}












