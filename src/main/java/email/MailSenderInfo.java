package email;

import java.util.Properties;

/**
 * @Auther: blue
 * @Date: 2019.10.3
 * @Description: 邮件信息类
 * @version: 1.0
 */
public class MailSenderInfo {
    private String mainServerHost;//服务器的主机号
    private String mailServletPort;//服务器的端口

    private String fromAddress;//发送者的地址
    private String userName;//发送者的邮箱的账号
    private String password;//发送者邮箱的密码
    private String toAddress;//收件人的地址
    private String subject;//邮件的的主题
    private String content;//邮件的的发送内容

    /*
        获得邮件的会话属性 session
        mail.smtp简单邮件传输的协议
     */
    public Properties getProperties(){
        Properties p = new Properties();
        p.put("mail.smtp.host",this.mainServerHost);//邮件主机服务器
        p.put("mail.smtp.port",this.mailServletPort);//邮件的端口号
        p.put("mail.smtp.auth","true"); //默认服务器和端口号是没问题的
        return p;
    }


    public String getMainServerHost() {
        return mainServerHost;
    }

    public void setMainServerHost(String mainServerHost) {
        this.mainServerHost = mainServerHost;
    }

    public String getMailServletPort() {
        return mailServletPort;
    }

    public void setMailServletPort(String mailServletPort) {
        this.mailServletPort = mailServletPort;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
