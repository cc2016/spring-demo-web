package com.demo.commons.spring;

/**
 * 返回响应code的定义类
 *
 */
public class ResponseCodes {
    /**
     * 通用的成功code
     */
    public static final int SUCCESS = 200;

    // 错误码定义
    // =-=-=-=-=-=-= -1000 -> -1999 系统或框架错误(数据库错误,参数错误等) =-=-=-=-=-=-=
    public static final int ERROR_DEFAULT_ERROR = -1000;
    public static final int ERROR_PARAM = -1001;

    // =-=-=-=-=-=-= -2000 -> -9999 业务错误(用户未登录,不符合某些业务需求等) =-=-=-=-=-=-=
    // -2000 -> -2099 通用业务错误
    public static final int ERROR_NOLOGIN = -2001;

    public static final int ERROR_ARTISAN = -2100; // 手艺人的错误code -2100-> -2199

    public static final int ERROR_USER = -3100; // 用户的错误code -3100-> -3199

    public static final int ERROR_USER_BEHAVIOR = -2200; // 用户行为业务逻辑错误code -2200 - -2299

    public static final int ERROR_IMAGE = -2300; // 上传图片的相关错误
}
