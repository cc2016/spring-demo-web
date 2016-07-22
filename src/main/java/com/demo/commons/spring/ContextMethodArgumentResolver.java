package com.demo.commons.spring;

import com.demo.commons.constants.SpringContents;
import org.springframework.core.MethodParameter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 将context的环境变量注入到spring mvc的方法中
 *
 * @author chen.lin
 * @date 14/11/26
 * @time 上午10:08
 */
public class ContextMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public boolean supportsParameter(MethodParameter parameter) {
        // 只支持context对象的处理
        if (Context.class.equals(parameter.getParameterType())) {
            return true;
        }
        return false;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 将request中存放的对象拿出来
        return webRequest.getAttribute(SpringContents.SPRING_KEY_CONTEXT, RequestAttributes.SCOPE_REQUEST);
    }

    public static void main(String[] args){
        PathMatcher pathMatcher = new AntPathMatcher();
        boolean match = pathMatcher.match("/**", "/user/test/getUser.json");

        System.out.println(match);
    }
}
