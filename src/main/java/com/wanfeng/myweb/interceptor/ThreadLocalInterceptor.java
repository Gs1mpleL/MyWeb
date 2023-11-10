package com.wanfeng.myweb.interceptor;

import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class ThreadLocalInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadLocalInterceptor.class);
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ThreadLocalUtils.release();
        LOGGER.info("Thead:{} ThreadLocal已经清理",Thread.currentThread().getName());
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
