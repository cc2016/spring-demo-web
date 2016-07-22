package com.demo.commons.spring;

/**
 * spring方法的返回值
 *
 */
public class JSONResult {
    /**
     * 返回的数据对象
     */
    private Object data;

    /**
     * code
     */
    private int code;

    /**
     * 错误消息
     */
    private String message;

    public JSONResult() {}

    public JSONResult(Object data, int code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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
}
