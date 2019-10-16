package com.hzq.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: blue
 * @Date: 2019/10/16
 * @Description: 针对登录，注册中的验证的工具类
 * @version: 1.0
 */
public class VerifyUtil {


    /**
     * 检测邮箱地址是否合法
     * @param email 邮箱
     * @return true合法 false不合法
     */
    public boolean isEmail(String email){
        if (null == email || "".equals(email))
            return false;
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 检测账号是否满足3到9个任意数字或字母组成的账号
     * @param username 账号
     * @return  true合法 false不合法
     */
    public boolean isStandardFormUsername(String username) {
        if (null == username || "".equals(username))
            return false;
        Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5_a-zA-Z0-9]{3,9}$");
        Matcher m = p.matcher(username);
        return m.matches();
    }

    /**
     * 检测密码是否满足数字和字母组成的任意6到9位
     * @param password 密码
     * @return true合法 false不合法
     */
  public boolean isStandardFormPassword(String password) {
        if (null == password || "".equals(password))
            return false;
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{6,9}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }








}
