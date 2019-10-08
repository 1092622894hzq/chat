package com.hzq.utils;

import java.util.Random;

/**
 * @Auther: blue
 * @Date: 2019/10/4
 * @Description: com.hzq.utils
 * @version: 1.0
 */
public class RandomUtil {

    /**
     * 根据传入的位数，产生多少位随机数
     * @param size 几位数
     * @return 返回随机数字符串
     */
    public static String CreateRandom(int size) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }
}
