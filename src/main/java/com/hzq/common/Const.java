package com.hzq.common;

import org.apache.commons.lang3.StringUtils;

/**
 * @Auther: blue
 * @Date: 2019/9/17
 * @Description: com.hzq.common
 * @version: 1.0
 */
public class Const {
    public static final String ACCESS_TOKEN = "accessToken";  //用户token
    public static final String REFRESH_TOKEN = "refreshToken"; //刷新token
    public static final String CURRENT_USER = "currentUser";  //当前用户
    public static final String EMAIL = "email"; //邮箱
    public static final Integer AUTHORITY = 0 ; //表示微信官方
    public static final String AUTHORITY_NAME = "微信官方";
    public static final String STANDARD_FORMAT = "yyyy:MM:dd HH:mm:ss"; //时间格式
    public static final Integer ONLINE = 1;  //在线
    public static final Integer OFFLINE = 0; //下线
    public static final Integer INVISIBLE = 2; //隐身
    public static final Integer TEXT = 0; //文本
    public static final Integer MARK_AS_READ = 1; //已读
    public static final Integer MARK_AS_UNREAD = 0; //未读
    public static final Integer DELETE = -1; //删除
    public static final Integer DEFAULT_GROUP = 0; //默认分组
    public static final Integer ZERO = 0; //置零
    public static final String File_PATH = "D:\\images";  //文件路径
    public static final String USERNAME = "username"; //账号
    public static final String PASSWORD = "password"; //密码
    public static final String NEW_PASSWORD = "newPassword"; //旧密码
    public static final String OLD_PASSWORD = "oldPassword"; //新密码
    public static final Integer IS_FRIEND = 0; //是否朋友
    public static final String DEFAUL_AVATAR = "default.png"; //默认头像
    public static final String CURRENT_CONNECT_ID = "uid"; //当前用户id

}
