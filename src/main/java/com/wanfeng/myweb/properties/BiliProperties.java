package com.wanfeng.myweb.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 哔哩哔哩配置
 */
@Component
@Data
@Configuration
@ConfigurationProperties(prefix = "bili")
public class BiliProperties {
    /** 代表所需要投币的数量 */
    static private Integer coin;
    /** 送出即将过期的礼物 true 默认送出*/
    static private boolean gift;
    /** 要将银瓜子转换成硬币 true 默认转换*/
    static private boolean s2c;
    /** 自动使用B币卷 */
    static private String autoBiCoin;
    /** 用户设备的标识 */
    static private String platform;
    private String biliJct;
    private String totalCookie;
    private String guessCoin;
    /* 根据totalCookie解析出biliJct，因为这个字段用于scrf*/
    public void setCookie(String totalCookie) throws Exception {
        String regStr = "bili_jct=(.*?); ";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(totalCookie);
        if (matcher.find()) {
            biliJct = matcher.group(0).replace("bili_jct=","").replace("; ","");
        }else {
            throw new Exception("cookie中未解析出bili_jct字段");
        }
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        BiliProperties.platform = platform;
    }

    public String getAutoBiCoin() {
        return autoBiCoin;
    }

    public void setAutoBiCoin(String autoBiCoin) {
        BiliProperties.autoBiCoin = autoBiCoin;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        BiliProperties.coin = coin;
    }

    public boolean isGift() {
        return gift;
    }

    public void setGift(boolean gift) {
        BiliProperties.gift = gift;
    }

    public boolean isS2c() {
        return s2c;
    }

    public void setS2c(boolean s2c) {
        BiliProperties.s2c = s2c;
    }

}
