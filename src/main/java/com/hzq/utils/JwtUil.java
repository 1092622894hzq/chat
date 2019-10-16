package com.hzq.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: blue
 * @Date: 2019/9/30
 * @Description: 生成token和解密token工具类
 * @version: 1.0
 */
public class JwtUil {
    //过期时间15分钟
    private static final long EXPIRE_TIME = 15 * 60 * 1000;
    //token私钥
    private static final String TOKEN_SECRET = "df1se23g2345sa870jt567hg23tg3g454ga43f4y";

    /**
     * 生成签名
     * @param username 用户名
     * @param id 用户id
     * @return 返回加密的token
     */
    public static String sign(String username, Integer id) {
        try {
        //私钥机加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //设置头部信息
        Map<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //playload中公共的声明中附带username,id信息
        return JWT.create()
                .withHeader(header)
                .withClaim("username", username)
                .withClaim("id", id)
                .sign(algorithm);

        } catch (UnsupportedEncodingException e) {
            System.out.println("不支持该加密");
            return null;
        }
    }

    /**
     * 验证token是否正确
     * @param token 密钥
     * @return boolean
     */
    public static boolean verify(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *  获取token中的用户名
     * @param token 所需的token
     * @return 返回用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            System.out.println("解码失败");
            return null;
        }
    }

    /**
     * 获取token中的id
     * @param token 所需的tooken
     * @return 返回用户id
     */
    public static Integer getId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("id").asInt();
        } catch (JWTDecodeException e) {
            System.out.println("解码失败");
            return null;
        }
    }
}







































