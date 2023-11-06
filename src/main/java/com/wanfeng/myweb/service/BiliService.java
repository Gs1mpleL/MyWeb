package com.wanfeng.myweb.service;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.Utils.BiliRequest;

import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.properties.BiliProperties;
import com.wanfeng.myweb.properties.PushToIphoneProperties;
import com.wanfeng.myweb.service.biliTask.*;
import com.wanfeng.myweb.vo.PushIphoneVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BiliService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiliService.class);
    /** 访问成功 */
    private static final String SUCCESS = "0";

    @Autowired
    private BiliUserData biliUserData;
    @Autowired
    private PushIphoneService pushIphoneService;
    @Autowired
    private DailyTask dailyTask;
    @Autowired
    private MangaTask mangaTask;
    @Autowired
    private BiliLiveTask biliLiveTask;
    @Autowired
    private Silver2CoinTask silver2CoinTask;
    @Autowired
    private CollectVipGift collectVipGift;
    @Autowired
    private BiliCoinApply biliCoinApply;
    @Autowired
    private ThrowCoinTask throwCoinTask;
    @Autowired
    private BiliProperties biliProperties;

    public void startBiliTask(){
        final List<String> list = new ArrayList<>();
        biliProperties.setCookie(biliProperties.getBiliJct(), biliProperties.getSESSDATA(), biliProperties.getDedeUserID());

        if(check()){
            LOGGER.info("用户名: {}", biliUserData.getUname());
            biliUserData.info("用户名: {}", biliUserData.getUname());
            LOGGER.info("硬币: {}", biliUserData.getMoney());
            biliUserData.info("硬币: {}", biliUserData.getMoney());
            LOGGER.info("经验: {}", biliUserData.getCurrentExp());
            biliUserData.info("经验: {}", biliUserData.getCurrentExp());
            dailyTask.run();
            mangaTask.run();
            silver2CoinTask.run();
            biliLiveTask.run();
            collectVipGift.run();
            biliCoinApply.run();
            throwCoinTask.run();
            biliSend();
        } else {
            throw  new RuntimeException("账户已失效，请在Secrets重新绑定你的信息");
        }
    }



    /**
     * 检查用户的状态
     */
    public boolean check(){
        JSONObject jsonObject = BiliRequest.get("https://api.bilibili.com/x/web-interface/nav");
        JSONObject object = jsonObject.getJSONObject("data");
        String code = jsonObject.getString("code");
        if(SUCCESS.equals(code)){
            /* 用户名 */
            biliUserData.setUname(object.getString("uname"));
            /* 账户的uid */
            biliUserData.setMid(object.getString("mid"));
            /* vip类型 */
            biliUserData.setVipType(object.getString("vipType"));
            /* 硬币数 */
            biliUserData.setMoney(object.getString("money"));
            /* 经验 */
            biliUserData.setCurrentExp(object.getJSONObject("level_info").getString("current_exp"));
            /* 大会员状态 */
            biliUserData.setVipStatus(object.getString("vipStatus"));
            /* 钱包B币卷余额 */
            biliUserData.setCouponBalance(object.getJSONObject("wallet").getString("coupon_balance"));
            return true;
        }
        return false;
    }

    public void biliSend(){
        String sendMsg = biliUserData.getSendMsg();
        String title = "哔哩哔哩";
        String groupName = "哔哩哔哩";
        try{
            String pushRep = pushIphoneService.push(new PushIphoneVo(title, sendMsg, groupName));
            if(Objects.equals(pushRep, "ok")){
                LOGGER.info("推送Iphone正常");
                biliUserData.info("推送Iphone正常");
            } else{
                LOGGER.info("推送Iphone失败");
            }
        } catch (Exception e){
            LOGGER.error("推送Iphone错误 -- "+e);
            biliUserData.info("推送Iphone错误 -- "+e);
        }finally {
            biliUserData.setSendMsg("");
        }
    }
}
