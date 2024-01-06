package com.wanfeng.myweb.crawler.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "push.iphone")
public class PushProperties {
    private String iphoneBaseUrl;
    private String macBaseUrl;
    private String icon;
}
