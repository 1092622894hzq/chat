package com.hzq.vo;

/**
 * @Auther: blue
 * @Date: 2019/10/18
 * @Description: 好友信息vo对象
 * @version: 1.0
 */
public class FriendVo {
    /*
    用户为好友起的昵称
     */
    private String friendName;
    /*
    用户为好友设定的分组
     */
    private String friendGroup;
    /*
    好友在主表中的id
     */
    private Integer friendId;
    /*
    用户在主表中的id
     */
    private Integer userId;
    /*
    是否别好友删除 0 没有 其他数字都是被删除的标志
     */
    private Integer isDelete;

    /*
    好友的账号名
     */
    private String username;

    /*
   申请人的性别 性别 1 表示男 0 表示女
    */
    private Integer sex;
    /*
    好友的个性签名
     */
    private String sign;
    /*
    好友的头像
     */
    private String avatar;
    /*
     申请人的昵称
     */
    private String nickname;

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendGroup() {
        return friendGroup;
    }

    public void setFriendGroup(String friendGroup) {
        this.friendGroup = friendGroup;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
