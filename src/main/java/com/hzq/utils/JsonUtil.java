package com.hzq.utils;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Type;

/**
 * @Auther: blue
 * @Date: 2019/10/19
 * @Description: 将对象变为json和将json变为对象的工具类
 * @version: 1.0
 */
public class JsonUtil {

    /**
     * 将json变为对象
     * @param json json数据
     * @param t 对象的class对象
     * @param <T> 泛型
     * @return 返回对象
     */
    public static  <T>  T getObjFromJson(String json, Class t) {
        return JSON.parseObject(json, (Type) t);
    }

    /**
     * 将对象变为json
     * @param <T> 泛型
     * @param t 对象
     * @return 返回json数据
     */
    public static <T> String toJson(T t) {
        return String.valueOf(JSON.toJSON(t));
    }

}
