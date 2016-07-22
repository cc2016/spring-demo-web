package com.demo.commons.spring;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.Locale;

/**
 * json格式返回值的处理器
 *
 */
public class JSONViewResolver extends UrlBasedViewResolver {
    JSONView view = new JSONView();

    public View resolveViewName(String viewName, Locale locale) throws Exception {
        return view;
    }

    public Class getViewClass() {
        return JSONView.class;
    }

}
