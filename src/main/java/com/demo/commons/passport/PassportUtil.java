package com.demo.commons.passport;

import com.demo.commons.tools.TokenUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * token解析获取用信息
 */
public class PassportUtil {
    public static LoginInfo getLoginInfo(HttpServletRequest request) {
        // 解密token,获取用户
        String token = request.getHeader("pra-token");
        int uid = TokenUtil.parseToken(token);
        LoginInfo info = new LoginInfo();
        info.setUserId(uid);
        info.setToken(token);
        return info;
    }
}
