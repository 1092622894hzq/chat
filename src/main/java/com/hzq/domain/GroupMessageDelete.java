package com.hzq.domain;

/**
 * @Auther: blue
 * @Date: 2019/11/8
 * @Description: com.hzq.domain
 * @version: 1.0
 */
public class GroupMessageDelete {
    /*
    群id
     */
    private Integer groupId;
    /*
    用户id
     */
    private Integer userId;
    /*
    消息id
     */
    private Integer groupMessageId;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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
}
