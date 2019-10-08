package com.hzq.domain;

import java.sql.Timestamp;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.domain
 * @version: 1.0
 */
public class Apply {
    private Integer id;
    private Integer fromId;
    private Integer toId;
    private String applyReason;
    private Integer applyStatus;
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
