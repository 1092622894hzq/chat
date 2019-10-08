package email;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * @Auther: blue
 * @Date: 2019.10.3
 * @Description: 核心类：发送邮件
 * @version: 1.0
 */
public class SimpleMailSender {
    /*
        发送html格式的邮件
        mailInfo:待发送邮件信息
     */
    public boolean sendHtmlMail(MailSenderInfo mailInfo){
        //1.判断是否需要身份验证
        MyAuthenticator authenticator = null;//验证类
        Properties pro = mailInfo.getProperties();//拿到主机和端口
        //2.验证类的实例化
        authenticator = new MyAuthenticator(mailInfo.getUserName(),mailInfo.getPassword());
        //3.根据邮件的会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro,authenticator);
        //4.通过session的会话创建我们的邮件信息
        try {
            Message mailMessage = new MimeMessage(sendMailSession);//创建邮件信息
            //5.创建发送邮件发送者的地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            //6.设置邮件的发送者，并把发件者地址设置到邮件当中
            mailMessage.setFrom(from);
            //7.设置接收者的地址信息
            Address to = new InternetAddress(mailInfo.getToAddress());
            //8.设置接收形式          //表示接收者地址类型     接收者的地址
            mailMessage.setRecipient(Message.RecipientType.TO,to);//BCC:密送..CC:抄送.. TO:直接发送
            //9.设置邮件的主题
            mailMessage.setSubject(mailInfo.getSubject());
            //10.设置发送时间
            mailMessage.setSentDate(new Date());
            //11.创建一个容器类.装邮件信息
            Multipart mailPart = new MimeMultipart();
            //12.创建一个HTML容器 bodyPart
            BodyPart html = new MimeBodyPart();
            html.setContent(mailInfo.getContent(),"text/html;charset=utf-8");
            mailPart.addBodyPart(html);
            //设置内容
            mailMessage.setContent(mailPart);
            //设置 发送html格式的邮件头信息
            MailcapCommandMap mc=(MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);
            //发送文件
            Transport.send(mailMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
