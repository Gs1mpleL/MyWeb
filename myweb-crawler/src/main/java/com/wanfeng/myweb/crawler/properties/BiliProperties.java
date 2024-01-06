package com.wanfeng.myweb.crawler.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 哔哩哔哩配置
 */
@Component
@Data
@Configuration
@ConfigurationProperties(prefix = "bili")
public class BiliProperties {
    /** 代表所需要投币的数量 */
    private Integer coin;
    /** 送出即将过期的礼物 true 默认送出 */
    private boolean gift;
    /** 要将银瓜子转换成硬币 true 默认转换 */
    private boolean s2c;
    /** 自动使用B币卷 */
    private String autoBiCoin;
    /** 用户设备的标识 */
    private String platform;
    /** 竞猜的硬币花费 **/
    private String guessCoin;
}
