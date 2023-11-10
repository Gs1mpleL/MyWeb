package com.wanfeng.myweb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.Utils.HttpUtils.BiliHttpUtils;

import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
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
    private BiliHttpUtils biliHttpUtils;
    @Autowired
    private CompetitionGuessTask competitionGuessTask;
    @Autowired
    private BiliProperties biliProperties;
    @Override
    public void doTask(String totalCookie) throws Exception {
        ThreadLocalUtils.put("biliUserData",new BiliUserData(totalCookie));
        biliTask(false);
    }
    public void biliTask(boolean isInnerJob) throws Exception {
        if (isInnerJob){
            ThreadLocalUtils.put("biliUserData",new BiliUserData(biliProperties.getMyTotalCookie()));
        }
        BiliUserData biliUserData = ThreadLocalUtils.get("biliUserData", BiliUserData.class);
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
            collectVipGift.run();
            biliCoinApply.run();
            throwCoinTask.run();
            competitionGuessTask.run();
            biliLiveTask.run();
            biliSend();
        } else {
            biliUserData.setSendMsg("账户已失效，请在Secrets重新绑定你的信息");
            biliSend();
            throw  new RuntimeException("账户已失效，请在Secrets重新绑定你的信息");
        }
    }



    /**
     * 检查用户的状态
     */
    public boolean check(){
BiliUserData biliUserData = ThreadLocalUtils.get("biliUserData", BiliUserData.class);
        JSONObject jsonObject = biliHttpUtils.get("https://api.bilibili.com/x/web-interface/nav");
        JSONObject object = jsonObject.getJSONObject("data");
        String code = jsonObject.getString("code");
        String SUCCESS = "0";
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
BiliUserData biliUserData = ThreadLocalUtils.get("biliUserData", BiliUserData.class);
        String sendMsg = biliUserData.getSendMsg();
        String title = "哔哩哔哩";
        String groupName = "哔哩哔哩";
        try{
            String pushRep = pushIphoneService.pushIphone(new PushVO(title, sendMsg, groupName));
            if(Objects.equals(pushRep, "推送成功")){
                LOGGER.info("推送Iphone正常");
                biliUserData.info("推送Iphone正常");
            } else{
                LOGGER.info("推送Iphone失败");
            }
        } catch (Exception e){
            LOGGER.error("推送Iphone错误 -- "+e);
        }
    }


}
