package com.demo.commons.viewcontrol;

import javax.servlet.*;
import java.io.IOException;

/**
 *
 */
public class ViewControlFilter implements Filter {

    private ViewControlService service;

    public void init(FilterConfig filterConfig) throws ServletException {
        String maxKey = filterConfig.getInitParameter("max");
        String intervalKey = filterConfig.getInitParameter("interval");

        service = new ViewControlService(maxKey, intervalKey);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // TODO 如果有代理的话,那么获取ip的方式需要修改
        if (service.addView(request.getRemoteAddr())) {
            chain.doFilter(request, response);
        }
    }

    public void destroy() {

    }


}
