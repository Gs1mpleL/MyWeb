package com.wanfeng.myweb.config;

import lombok.Data;

@Data
public class YuanshenConfig {
    public static final String ACT_ID = "e202311201442471"; // 切勿乱修改
    public static final String USER_AGENT_TEMPLATE = "Mozilla/5.0 (iPhone; CPU iPhone OS 14_0_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) miHoYoBBS/%s";
//    public static final String refererURL = String.format("https://webstatic.mihoyo.com/bbs/event/signin-ys/index.html?bbs_auth_required=%s&act_id=%s&utm_source=%s&utm_medium=%s&utm_campaign=%s", true, ACT_ID, "bbs", "mys", "icon");
    public static final String refererURL = " https://act.mihoyo.com/";
    public static final String ROLE_URL = String.format("https://api-takumi.mihoyo.com/binding/api/getUserGameRolesByCookie?game_biz=%s", "hk4e_cn");
    public static final String SIGN_URL = "https://api-takumi.mihoyo.com/event/luna/sign";
    public static final String INFO_URL = "https://api-takumi.mihoyo.com/event/luna/info";
    public static final String REGION = "cn_gf01"; // 切勿乱修改
    public static final String AWARD_URL = String.format("https://api-takumi.mihoyo.com/event/bbs_sign_reward/home?act_id=%s", ACT_ID);
    public static final String COMMUNITY_SIGN_SALT = "t0qEgfub6cvueAPgR5m9aQWWVciEer7v";
    public static final String SIGN_CLIENT_TYPE = "5";
    public static final String APP_VERSION = "2.58.2";
    public static final String SIGN_SALT = "KTJQGN2a2Trqk0tcQZS6JV3rU7CnV8Q6";
}
