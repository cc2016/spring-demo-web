package com.demo.commons.spring;

import com.demo.commons.constants.SpringContents;
import com.demo.commons.logger.APILog;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理环境变量的拦截器
 *
 * @author chen.lin
 * @date 14/11/26
 * @time 上午10:03
 */
public class ContextInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();

        // 在spring方法执行前的处理动作
        // TODO 预处理
        Context context = new Context();
        context.setRequestContent(request);

        // 将环境变量放到request中
        request.setAttribute(SpringContents.SPRING_KEY_CONTEXT, context);
        request.setAttribute(SpringContents.SPRING_KEY_TIME_START, watch);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在spring方法执行后的处理动作
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // TODO 在返回值返回之后,执行此方法
        StopWatch watch = (StopWatch) request.getAttribute(SpringContents.SPRING_KEY_TIME_START);

        if (watch != null) {
            watch.stop();

            // TODO 日志记录执行时间
            APILog.accessLog(" run url :: " + request.getRequestURL() + " | time:: " + watch.getTime() + "ms");
        }
    }
}
