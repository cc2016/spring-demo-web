package com.demo.commons.spring;

import com.demo.commons.exception.Error;
import com.demo.commons.exception.ExceptionProxy;
import com.demo.commons.tools.ResponseUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理切面
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    // 是否需要处理异常
    private static boolean needExceptionAdvice = true;

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public String handleException(HttpServletRequest request, HttpServletResponse response, Throwable ex) throws Throwable {

        if (!needExceptionAdvice) {
            throw ex;
        }

        // 异常处理
        Error error = ExceptionProxy.handle(ex, request);

        ResponseUtil.writeResponse(response, error);
        return null;
    }

    public boolean isNeedExceptionAdvice() {
        return needExceptionAdvice;
    }

    public void setNeedExceptionAdvice(boolean needExceptionAdvice) {
        ExceptionControllerAdvice.needExceptionAdvice = needExceptionAdvice;
    }
}
