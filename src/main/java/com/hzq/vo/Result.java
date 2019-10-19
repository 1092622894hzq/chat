package com.hzq.vo;


import com.hzq.domain.*;

import java.util.List;
import java.util.Map;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: com.hzq.domain
 * @version: 1.0
 */
public class Result {
    private User user;
    private UserInfo userInfo;
    private List<FriendVo> friends;
    private List<Group> groups;
    private Map<Integer, List<ApplyVo>> applyMap;
    private Map<Integer, List<Message>> messageMap;
    private Map<Integer, List<GroupMessageContent>> groupContentMap;
    private CommonResult commonResult;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<FriendVo> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendVo> friends) {
        this.friends = friends;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Map<Integer, List<ApplyVo>> getApplyMap() {
        return applyMap;
    }

    public void setApplyMap(Map<Integer, List<ApplyVo>> applyMap) {
        this.applyMap = applyMap;
    }

    public Map<Integer, List<Message>> getMessageMap() {
        return messageMap;
    }

    public void setMessageMap(Map<Integer, List<Message>> messageMap) {
        this.messageMap = messageMap;
    }

    public Map<Integer, List<GroupMessageContent>> getGroupContentMap() {
        return groupContentMap;
    }

    public void setGroupContentMap(Map<Integer, List<GroupMessageContent>> groupContentMap) {
        this.groupContentMap = groupContentMap;
    }

    public CommonResult getCommonResult() {
        return commonResult;
    }

    public void setCommonResult(CommonResult commonResult) {
        this.commonResult = commonResult;
    }
}
