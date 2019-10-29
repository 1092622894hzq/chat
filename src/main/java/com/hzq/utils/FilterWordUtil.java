package com.hzq.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: blue
 * @Date: 2019/10/24
 * @Description: 过滤敏感词
 * @version: 1.0
 */
public class FilterWordUtil {

    private static Integer keywordsCount = 0;
    private static Pattern pattern;

    private static void initPattern() {
        StringBuilder patternBuilder = new StringBuilder();
        try {
            InputStream in = FilterWordUtil.class.getClassLoader().getResourceAsStream("keywords.properties");
            Properties property = new Properties();
            property.load(in);
            Enumeration<?> enu = property.propertyNames();
            patternBuilder.append("(");
            while (enu.hasMoreElements()) {
                String content = (String) enu.nextElement();
                patternBuilder.append(content).append("|");
                keywordsCount ++;
            }
            patternBuilder.deleteCharAt(patternBuilder.length() - 1);
            patternBuilder.append(")");
            pattern = Pattern.compile(patternBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String doFilter(String str) {
        Matcher m = pattern.matcher(str);
        return m.replaceAll("*");
    }

    public static void main(String[] args) throws IOException {
        initPattern();
        System.out.println(keywordsCount);
        String a = "中国共产党,五星红旗，中华人们共和国，日本鬼子";
        System.out.println(doFilter(a));
    }
}
