package com.demo.commons.exception;

import com.demo.commons.spring.ResponseCodes;

/**
 * 异常列表
 *
 */
public class Errors {

    // ------------------- 系统异常 -----------------
    /**
     * 默认异常
     */
    public static final Error DEFAULT_ERROR = new Error();

    /**
     * 参数错误
     */
    public static final Error PARAM_ERROR = new Error();


    static {
        DEFAULT_ERROR.setCode(ResponseCodes.ERROR_DEFAULT_ERROR);
        PARAM_ERROR.setCode(ResponseCodes.ERROR_PARAM);
    }
}
