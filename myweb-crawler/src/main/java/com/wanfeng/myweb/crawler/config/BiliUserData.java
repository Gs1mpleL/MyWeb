package com.wanfeng.myweb.crawler.config;


import com.wanfeng.myweb.api.dto.SystemConfigDto;
import com.wanfeng.myweb.common.exception.BizException;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户的一些个人信息
 */
@Data
public class BiliUserData {
    public static final String BILI_USER_DATA = "biliUserData";
    /** 登录账户的用户名 */
    private String uname;
    /** 登录账户的uid */
    private String mid;
    /** 代表账户的类型 */
    private String vipType;
    /** 硬币数 */
    private String money;
    /** 经验数 */
    private String currentExp;
    /** 大会员状态 */
    private String vipStatus;
    /** B币卷余额 */
    private String couponBalance;
    private String sendMsg = "";
    /** 认证 **/
    private String totalCookie;
    private String biliJct;
    private String refreshToken;

    /**
     * token可刷新
     */
    public BiliUserData(SystemConfigDto systemConfig) throws BizException {
        totalCookie = systemConfig.getBiliCookie();
        setCookie(systemConfig.getBiliCookie());
        this.refreshToken = systemConfig.getBiliRefreshToken();
    }

    /**
     * 只设置cookie，不能刷新token
     */
    public BiliUserData(String totalCookieFromWeb) throws BizException {
        totalCookie = totalCookieFromWeb;
        setCookie(totalCookieFromWeb);
    }

    public void setCookie(String totalCookie) throws BizException {
        String regStr = "bili_jct=(.*?); ";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(totalCookie);
        if (matcher.find()) {
            biliJct = matcher.group(0).replace("bili_jct=", "").replace("; ", "");
        } else {
            throw new BizException("cookie解析失败");
        }
    }

    public void info(String template, String addMsg) {
        addMsg = template.replace("{}", "「" + addMsg + " 」");
        addMsg = addMsg.replace("--", ":");
        sendMsg += "\n";
        sendMsg += addMsg;
    }

    public void info(String addMsg) {
        addMsg = addMsg.replace("--", ":");
        sendMsg += "\n";
        sendMsg += "「" + addMsg + "」";
    }
}
