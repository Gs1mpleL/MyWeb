package com.wanfeng.myweb.bilibili.config;

import com.wanfeng.common.service.SystemConfigService;
import com.wanfeng.common.service.impl.SystemConfigServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemConfig {
    @Bean
    public SystemConfigService getSystemCOnfigService(){
        return new SystemConfigServiceImpl();
    }
}
