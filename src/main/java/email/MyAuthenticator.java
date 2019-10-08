package email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @Auther: blue
 * @Date: 2019.10.3
 * @Description: 邮件验证类:身份验证类——邮箱帐户和密码的验证
 * @version: 1.0
 */
//继承一个验证的父类
public class MyAuthenticator extends Authenticator {
    String username = null; //账号
    String password = null; //密码

    public MyAuthenticator(){ //默认构造函数
    }

    public  MyAuthenticator(String username,String password){
        this.username = username;
        this.password = password;
    }
    //验证用户名和密码
    //继承父类的方法
    protected PasswordAuthentication  getPasswordAuthentication(){
        return new PasswordAuthentication(username,password);
    }

}
