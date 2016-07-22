package com.demo.commons.spring;

import com.demo.commons.constants.SpringContents;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 返回值处理
 *
 */
public class ReturnValueHandler extends ModelAttributeMethodProcessor {

    public ReturnValueHandler() {
        super(true);
    }

    /**
     * @param annotationNotRequired if "true", non-simple method arguments and
     *                              return values are considered model attributes with or without a
     *                              {@code @ModelAttribute} annotation.
     */
    public ReturnValueHandler(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return true;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        webRequest.setAttribute(SpringContents.SPRING_KEY_RETURN, returnValue, RequestAttributes.SCOPE_REQUEST);
        super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }
}
