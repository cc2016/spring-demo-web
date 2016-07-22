package com.demo.commons.passport;

/**
 * 用户登录信息
 *
 */
public class LoginInfo {
    private int userId;

    private String token;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
