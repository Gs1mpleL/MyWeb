package com.wanfeng.myweb.crawler.config;

import com.wanfeng.myweb.crawler.interceptor.ThreadLocalInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Bean
    public ThreadLocalInterceptor getThreadLocalInterceptor() {
        return new ThreadLocalInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(this.getThreadLocalInterceptor());

        // 所有路径都会被拦截
        interceptorRegistration.addPathPatterns("/**");

        // 添加不拦截的路径
//        interceptorRegistration.excludePathPatterns("");
    }
}
