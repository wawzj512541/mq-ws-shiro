package com.example.message.entity;

/**
 * @Description:
 * @Author: vesus
 * @CreateDate: 2018/5/28 下午5:46
 * @Version: 1.0
 */
public class RequestMessage {

    /***
     * 请求消息
     */
    private String message ;
    private Long userid;
    private Long toUserId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }
}
