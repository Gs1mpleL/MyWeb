package com.wanfeng.myweb.config;

import lombok.Data;
import org.springframework.stereotype.Component;

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
    public void setCookie(String totalCookie) throws BizException {
        String regStr = "bili_jct=(.*?); ";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(totalCookie);
        if (matcher.find()) {
            biliJct = matcher.group(0).replace("bili_jct=","").replace("; ","");
        }else {
            throw new BizException("cookie中未解析出bili_jct字段");
        }
    }
    public void info(String template, String addMsg){
        addMsg = template.replace("{}","「" + addMsg + " 」");
        addMsg = addMsg.replace("--",":");
        sendMsg+="\n";
        sendMsg+=addMsg;
    }
    public void info(String addMsg){
        addMsg = addMsg.replace("--",":");
        sendMsg+="\n";
        sendMsg+="「"+addMsg + "」";
    }

    public BiliUserData(String tc) throws BizException {
        totalCookie = tc;
        setCookie(tc);
    }
}
