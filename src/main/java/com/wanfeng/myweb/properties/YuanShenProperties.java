package com.wanfeng.myweb.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "yuanshen")
public class YuanShenProperties {
    private String cookie;
}
