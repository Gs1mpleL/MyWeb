package com.wanfeng.common.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("system")
@Data
public class SystemConfig {
    /* 项目启动是否推送Iphone*/
    private int bootAlarm = 0;
}
