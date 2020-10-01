package com.simplestomp.model;

/**
 * @Author: wonzeng
 * @CreateTime: 2020-09-26
 */
public class Information {
    private String message;

    public Information(){
        message = "hello world!";
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Content{" +
                "message='" + message + '\'' +
                '}';
    }
}
