package com.hzq.utils;

import email.MailSenderInfo;
import email.SimpleMailSender;


/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: com.hzq.utils
 * @version: 1.0
 */
public class SendEmailUtil {

    public static void sendEmail(String email,String subject, String content) {
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMainServerHost("smtp.qq.com"); // 邮件服务器主机
        mailInfo.setMailServletPort("25"); // 服务器端口  SMTP:单单的邮件传输协议  端口：25

        mailInfo.setUserName("1092622894@qq.com"); // 发送者用户名
        mailInfo.setPassword("qwuflpkuifzejchc"); //　发送者密码
        mailInfo.setFromAddress("1092622894@qq.com"); //发送者地址

        mailInfo.setToAddress(email);
        mailInfo.setSubject(subject);
        mailInfo.setContent("<div style='width:100%;height:100%;background:skyblue'>"+content+"</div>");


        SimpleMailSender sms = new SimpleMailSender();
        boolean flag = sms.sendHtmlMail(mailInfo); // 发送邮件
        if(flag){
            System.out.println("成功！");
        }else{
            System.out.println("失败！");
        }
    }


}
