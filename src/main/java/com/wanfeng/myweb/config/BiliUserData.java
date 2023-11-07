package com.wanfeng.myweb.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 用户的一些个人信息
 */
@Component
@Data
public class BiliUserData {

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

    private BiliUserData(){}
}
