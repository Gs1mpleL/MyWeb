package com.wanfeng.myweb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.Utils.BiliRequest;

import com.wanfeng.myweb.config.BiliData;
import com.wanfeng.myweb.properties.BiliProperties;
import com.wanfeng.myweb.service.BiliService;
import com.wanfeng.myweb.service.impl.biliTask.*;
import com.wanfeng.myweb.vo.PushVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Objects;

@Service
public class BiliServiceImpl implements BiliService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiliServiceImpl.class);
    @Autowired
    private BiliData biliData;
    @Autowired
    private PushServiceImpl pushIphoneService;
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
    @Autowired
    private BiliRequest biliRequest;
    @Autowired
    private CompetitionGuessTask competitionGuessTask;
    @Override
    public void doTask() {
        biliTask();
    }
    public void biliTask(){
        try {
            biliProperties.setCookie(biliProperties.getTotalCookie());
        }catch (Exception e){
            biliData.setSendMsg(e.getMessage());
            return;
        }
        if(check()){
            LOGGER.info("用户名: {}", biliData.getUname());
            biliData.info("用户名: {}", biliData.getUname());
            LOGGER.info("硬币: {}", biliData.getMoney());
            biliData.info("硬币: {}", biliData.getMoney());
            LOGGER.info("经验: {}", biliData.getCurrentExp());
            biliData.info("经验: {}", biliData.getCurrentExp());
            dailyTask.run();
            mangaTask.run();
            silver2CoinTask.run();
            collectVipGift.run();
            biliCoinApply.run();
            throwCoinTask.run();
            competitionGuessTask.run();
            biliLiveTask.run();
            biliSend();
        } else {
            biliData.setSendMsg("账户已失效，请在Secrets重新绑定你的信息");
            biliSend();
            throw  new RuntimeException("账户已失效，请在Secrets重新绑定你的信息");
        }
    }



    /**
     * 检查用户的状态
     */
    public boolean check(){
        JSONObject jsonObject = biliRequest.get("https://api.bilibili.com/x/web-interface/nav");
        JSONObject object = jsonObject.getJSONObject("data");
        String code = jsonObject.getString("code");
        String SUCCESS = "0";
        if(SUCCESS.equals(code)){
            /* 用户名 */
            biliData.setUname(object.getString("uname"));
            /* 账户的uid */
            biliData.setMid(object.getString("mid"));
            /* vip类型 */
            biliData.setVipType(object.getString("vipType"));
            /* 硬币数 */
            biliData.setMoney(object.getString("money"));
            /* 经验 */
            biliData.setCurrentExp(object.getJSONObject("level_info").getString("current_exp"));
            /* 大会员状态 */
            biliData.setVipStatus(object.getString("vipStatus"));
            /* 钱包B币卷余额 */
            biliData.setCouponBalance(object.getJSONObject("wallet").getString("coupon_balance"));
            return true;
        }
        return false;
    }

    public void biliSend(){
        String sendMsg = biliData.getSendMsg();
        String title = "哔哩哔哩";
        String groupName = "哔哩哔哩";
        try{
            String pushRep = pushIphoneService.pushIphone(new PushVO(title, sendMsg, groupName));
            if(Objects.equals(pushRep, "推送成功")){
                LOGGER.info("推送Iphone正常");
                biliData.info("推送Iphone正常");
            } else{
                LOGGER.info("推送Iphone失败");
            }
        } catch (Exception e){
            LOGGER.error("推送Iphone错误 -- "+e);
        }
    }


}
