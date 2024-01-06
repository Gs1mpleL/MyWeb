package com.wanfeng.myweb.crawler.interceptor;


import com.wanfeng.myweb.common.Utils.ThreadLocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ThreadLocalInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadLocalInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ThreadLocalUtils.release();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
