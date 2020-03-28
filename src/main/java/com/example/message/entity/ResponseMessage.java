package com.example.message.entity;

/**
 * @Description:
 * @Author: vesus
 * @CreateDate: 2018/5/28 下午5:47
 * @Version: 1.0
 */
public class ResponseMessage {



    public ResponseMessage(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    /**
     * 响应消息
     */
    private String message ;
    private String userId ;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
