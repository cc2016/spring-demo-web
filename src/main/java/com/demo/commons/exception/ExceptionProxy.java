package com.demo.commons.exception;

import com.demo.commons.logger.APILog;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理器
 * 框架层捕获异常
 */
public class ExceptionProxy {

    private static final Object EMPTYDATA = new Object();

    public static Error handle(Throwable ex, HttpServletRequest request) {
        // TODO 统一的异常处理,并返回给前端数据
        if (ex instanceof BaseAppException) {
            // 运行时业务异常
            BaseAppException e = (BaseAppException) ex;
            Error error = new Error();
            error.setCode(e.getCode());
            error.setMessage(e.getMessage());

            if (e.getData() == null) {
                error.setData(EMPTYDATA);
            } else {
                error.setData(e.getData());
            }

            APILog.info(ex.getMessage());

            return error;
        } else if (ex instanceof MissingServletRequestParameterException) {
            return Errors.PARAM_ERROR;
        }

        // 未知异常
        APILog.error("controller error :: ", ex);

        Error error = new Error();
        error.setCode(Errors.DEFAULT_ERROR.getCode());
        error.setMessage(ex.getMessage());

        return error;
    }
}
