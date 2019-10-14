package com.hzq.webSocket.stomp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: blue
 * @Date: 2019/10/14
 * @Description: 管理在线用户
 * @version: 1.0
 */
public class chatUserManger {

    private static Logger LOGGER;
    private static ConcurrentHashMap<Integer, String> USER_MAP;

    static {
        LOGGER = LoggerFactory.getLogger(chatUserManger.class);
        USER_MAP = new ConcurrentHashMap<>();
    }

    public void pushOnlineUser(String id) {

      //  USER_MAP.put(,id);
    }

}






















