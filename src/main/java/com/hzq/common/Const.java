package com.hzq.common;


/**
 * @Auther: blue
 * @Date: 2019/9/17
 * @Description: com.hzq.common
 * @version: 1.0
 */
public class Const {
    /*
    表示用户token
     */
    public static final String ACCESS_TOKEN = "accessToken";
    /*
    表示刷新token
     */
    public static final String REFRESH_TOKEN = "refreshToken";
    /*
    表示当前用户
     */
    public static final String CURRENT_USER = "currentUser";
    /*
    表示邮箱
     */
    public static final String EMAIL = "email";
    /*
    表示默认好友微信官方的类型
     */
    public static final Integer AUTHORITY = 0 ;
    /*
    表示默认好友卫微信官方的名字
     */
    public static final String AUTHORITY_NAME = "微信官方";
    /*
    表示时间格式
     */
    public static final String TIME_FORMAT = "yyyy:MM:dd HH:mm:ss";
    /*
    表示删除
     */
    public static final Integer DELETE = -1;
    /*
    表示默认分组
     */
    public static final Integer DEFAULT_GROUP = 0;
    /*
    表示将置零
     */
    public static final Integer ZERO = 0;
    /*
    表示文件存储的路径
     */
    public static final String File_PATH = "C:\\images";
    /*
    表示还是好友
     */
    public static final Integer IS_FRIEND = 0;
    /*
    表示默认头像
     */
    public static final String DEFAULT_AVATAR = "http://116.62.12.63:8080/images/default.png";
    /*
    表示当前用户id
     */
    public static final String CURRENT_CONNECT_ID = "uid";

    //用户表的常量
    /*
    用户下线
     */
    public static final Integer OFFLINE = 0;
    /*
    用户在线
    */
    public static final Integer ONLINE = 1;
    /*
    用户隐身
     */
    public static final Integer INVISIBLE = 2; //隐身
    /*
    用户账号
     */
    public static final String USERNAME = "username";
    /*
    用户密码
     */
    public static final String PASSWORD = "password";
    /*
    用户旧密码
     */
    public static final String OLD_PASSWORD = "oldPassword";
    /*
    用户新密码
     */
    public static final String NEW_PASSWORD = "newPassword";

    //消息表
    /*
    表示消息为文本类型
     */
    public static final Integer TEXT = 0;
    /*
    表示消息为文件类型
     */
    public static final Integer FILE = 1;
    /*
    表示消息为图片
     */
    public static final Integer PICTURE = 2;
    /*
    表示消息已读
     */
    public static final Integer MARK_AS_READ = 1;
    /*
    表示消息未读
     */
    public static final Integer MARK_AS_UNREAD = 0; //未读

    //申请表中的常量
    /*
    好友申请被拒绝
     */
    public static final Integer APPLY_REFUSE = -1;
    /*
    好友申请未被处理
     */
    public static final Integer APPLY_UNTREATED = 0;
    /*
    好友申请被同意
     */
    public static final Integer APPLY_AGREE = 1;


}
