package com.demo.commons.exception;

/**
 * 异常基类
 *
 */
public class BaseAppException extends RuntimeException {
    /**
     * 异常code
     */
    private int code;

    /**
     * 异常消息描述
     */
    private String message;

    private Object data;

    public BaseAppException() {

    }

    public BaseAppException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseAppException(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
