package com.wanfeng.myweb.service.biliTask;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.Utils.BiliRequest;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.properties.BiliProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.TimeZone;
@Component
public class CollectVipGift implements Task {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(CollectVipGift.class);
    /** 获取DATA对象 */
    @Autowired
    private BiliUserData biliUserData;

    @Autowired
    private BiliRequest biliRequest;

    @Autowired
    private BiliProperties biliProperties;

    /** 不是大会员 */
    private static final String NOT_VIP = "0";
    /** 是大会员 */
    private static final String IS_VIP = "1";
    /** 年度大会员 */
    private static final String YEAR_VIP = "2";

    @Override
    public void run() {
        try{
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            int day = cal.get(Calendar.DATE);
            String vipType = queryVipStatusType();

            /* 每个月1号，年度大会员领取权益 */
            if(day==1&&YEAR_VIP.equals(vipType)){
                vipPrivilege(1);
                vipPrivilege(2);
            }else {
                LOGGER.info("领取年度大会员礼包 -- {}","失败,每月一号可执行");
                biliUserData.info("领取年度大会员礼包 -- {}","失败,每月一号可执行");
            }
        } catch (Exception e){
            LOGGER.error("领取年度大会员礼包错误 -- "+e);
            biliUserData.info("领取年度大会员礼包错误 -- "+e);
        }
    }

    /**
     * 领取年度大会员B卷和大会员福利/权益
     * @param type [{1,领取大会员B币卷}, {2,领取大会员福利}]
     * 
     */
    public void vipPrivilege(Integer type) {
        String body = "type=" + type
                + "&csrf=" + biliProperties.getBiliJct();
        JSONObject jsonObject = biliRequest.post("https://api.bilibili.com/x/vip/privilege/receive", body);
        Integer code = jsonObject.getInteger("code");
        if (0 == code) {
            if (type == 1) {
                LOGGER.info("领取年度大会员每月赠送的B币券 -- 成功");
                biliUserData.info("领取年度大会员每月赠送的B币券 -- 成功");
            } else if (type == 2) {
                LOGGER.info("领取大会员福利/权益 -- 成功");
                biliUserData.info("领取大会员福利/权益 -- 成功");
            }

        } else {
            LOGGER.warn("领取年度大会员每月赠送的B币券/大会员福利 -- 失败 -- " + jsonObject.getString("message"));
            biliUserData.info("领取年度大会员每月赠送的B币券/大会员福利 -- 失败 -- " + jsonObject.getString("message"));
        }
    }

    /**
     * 检查用户的会员状态。如果是会员则返回其会员类型。
     * @return Integer
     * 
     */
    public String queryVipStatusType() {
        if (IS_VIP.equals(biliUserData.getVipStatus())) {
            return biliUserData.getVipType();
        } else {
            return NOT_VIP;
        }
    }
}