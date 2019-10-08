package com.hzq.webSocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;

@ServerEndpoint(value = "/chat")
@Component
public class WebsocketServer {
    private Session session;
    private static int count=0;
 
    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session=session;
        //Users用来存放数据，user是该类的一个静态HashMap变量
//        if(Users.user.get(userName)==null) {
//            Users.user.put(userName, this);
//            count++;
//        }
        System.out.println(session.getQueryString());
        session.getBasicRemote().sendText("系统消息：当前在线人数:"+ ++count);
    }
 
    @OnMessage
    public void onMessage(Session session,String msg) throws IOException {
//        for(WebsocketServer server:Users.user.values()){
//            server.session.getBasicRemote().sendText(msg);
//        }
        System.out.println("msg:"+":"+msg);
    }

    @OnClose
    public void close(Session session){
        myClose(session);
    }

    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("连接用户id为:"+session.getId()+"时发生错误");
        error.printStackTrace();
        myClose(session);
    }

    //手动控制是否关闭
    private static void myClose(Session session){
//        onlineCount--;
//        System.out.println("当前在线人数为："+onlineCount); //找了很久的的错误，移除时不能拿全局的id，根据要删除的session找到对应的key
//        try {
//            session.close();
//            Integer key = null;
//            for(Map.Entry entry:sessionMap.entrySet()){
//                if(session.equals(entry.getValue())) {
//                    key = (Integer) entry.getKey();
//                }
//            }
//            if (key!=null) {
//                System.out.println("当前要移除的key--->"+key);
//                sessionMap.remove(key);
//                namesMap.remove(key);
//            }else {
//                System.out.println("找不到对应的key去移除");
//            }
//            System.out.println("执行关闭session连接");
//        } catch (IOException e) {
//            System.out.println("关闭连接");
//        }

    }

    //发送自己
    public static void send(Session session,String msg){
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //发给其他人
    private void sendOther(Session session,String msg){
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            System.out.println("执行发送给别人发生错误:"+msg+session);
            e.printStackTrace();
        }
    }
}