package com.hzq.domain;

/**
 * @Auther: blue
 * @Date: 2019/11/1
 * @Description: 密保问题
 * @version: 1.0
 */
public class SecretSecurity {
    /*
    密保问题1
     */
    private String question1;
    /*
    密保答案1
     */
    private String answer1;
    /*
    密保问题2
     */
    private String question2;
    /*
    密保答案2
     */
    private String answer2;
    /*
    所属用户id
     */
    private Integer userId;

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
