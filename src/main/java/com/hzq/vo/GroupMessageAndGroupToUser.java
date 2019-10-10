package com.hzq.vo;

/**
 * @Auther: blue
 * @Date: 2019/10/10
 * @Description: 群消息和群和用户关系的联合查询
 * @version: 1.0
 */
public class GroupMessageAndGroupToUser {

    private Integer userId;
    private Integer groupMessageId;

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
