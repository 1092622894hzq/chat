package com.hzq.utils;

/**
 * @Auther: blue
 * @Date: 2019/10/20
 * @Description: 处理字符串的工具类
 * @version: 1.0
 */
public class StringUtil {

    /**
     * 切割虚拟路径得到电脑中的图片保存路径
     * @param url 虚拟图片路径
     * @return 返回真实的图片路径
     */
    public static String getPhotoUrl(String url) {
        url = "C:"+url.substring(24);
        System.out.println(url);
        return url;
    }


}
