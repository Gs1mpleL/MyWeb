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
public class BiliCoinApply implements Task {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(BiliCoinApply.class);
    /** 获取DATA对象 */
    @Autowired
    private BiliUserData biliUserData;

    @Autowired
    private BiliRequest biliRequest;

    @Autowired
    private BiliProperties biliProperties;
    /** 28号代表月底 */
    private static final int END_OF_MONTH = 28;
    /** 代表获取到正确的json对象 code */
    private static final String SUCCESS = "0";

    @Override
    public void run() {
        try{
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            int day = cal.get(Calendar.DATE);
            /* B币券余额 */
            double couponBalance = Double.parseDouble(biliUserData.getCouponBalance());
            if (day == END_OF_MONTH && couponBalance > 0) {
                switch (biliProperties.getAutoBiCoin()){
                    case "1" : doCharge(couponBalance);break;
                    case "2" : doMelonSeed((int) couponBalance);break;
                    default: break;
                }
            }
        } catch (Exception e){
            LOGGER.error("使用B币卷部分异常 -- "+e);
            biliUserData.info("使用B币卷部分异常 -- "+e);
        }
    }

    /**
     * 月底自动给自己充电。仅充会到期的B币券，低于2的时候不会充
     * 
     */
    public void doCharge(Double couponBalance) {
        /*
         * 判断条件 是月底&&b币券余额大于2&&配置项允许自动充电
         */
        if(couponBalance < 2){
            LOGGER.warn("B币卷数量: "+ couponBalance + " -- 无法给自己充电");
            biliUserData.info("B币卷数量: "+ couponBalance + " -- 无法给自己充电");
            return ;
        }
        /* 被充电用户的userID */
        String userId = biliUserData.getMid();
        String body = "elec_num=" + couponBalance * 10
                + "&up_mid=" + userId
                + "&otype=up"
                + "&oid=" + userId
                + "&csrf=" + biliProperties.getBiliJct();

        JSONObject jsonObject = biliRequest.post("http://api.bilibili.com/x/ugcpay/trade/elec/pay/quick", body);

        Integer resultCode = jsonObject.getInteger("code");
        if (resultCode == 0) {
            JSONObject dataJson = jsonObject.getJSONObject("biliData");
            LOGGER.debug(dataJson.toString());
            Integer statusCode = dataJson.getInteger("status");
            if (statusCode == 4) {
                LOGGER.info("月底了，给自己充电成功啦，送的B币券没有浪费啦");
                biliUserData.info("月底了，给自己充电成功啦，送的B币券没有浪费啦");
                LOGGER.info("本次给自己充值了: " + couponBalance * 10 + "个电池");
                biliUserData.info("本次给自己充值了: " + couponBalance * 10 + "个电池");
                /* 获取充电留言token */
                String orderNo = dataJson.getString("order_no");
                chargeComments(orderNo);
            } else {
                LOGGER.warn("充电失败 -- " + jsonObject);
                biliUserData.info("充电失败 -- " + jsonObject);
            }

        } else {
            LOGGER.warn("充电失败了啊 -- " + jsonObject);
            biliUserData.info("充电失败了啊 -- " + jsonObject);
        }
    }

    /**
     * 自动充电完，添加一条评论
     * @param token 订单id
     * 
     */
    public void chargeComments(String token) {

        String requestBody = "order_id=" + token
                + "&message=" + "BilibiliTask自动充电"
                + "&csrf=" + biliProperties.getBiliJct();
        JSONObject jsonObject = biliRequest.post("http://api.bilibili.com/x/ugcpay/trade/elec/message", requestBody);
        LOGGER.debug(jsonObject.toString());
    }

    /**
     * 用B币卷兑换成金瓜子
     * @param couponBalance 传入B币卷的数量
     */
    public void doMelonSeed(Integer couponBalance){
        String body = "platform=pc"
                + "&pay_bp=" + couponBalance * 1000
                + "&context_id=1"
                + "&context_type=11"
                + "&goods_id=1"
                + "&goods_num=" + couponBalance
                + "&csrf=" + biliProperties.getBiliJct();
        JSONObject post = biliRequest.post("https://api.live.bilibili.com/xlive/revenue/v1/order/createOrder", body);
        String msg ;
        /* json对象的状态码 */
        String code = post.getString("code");
        if(SUCCESS.equals(code)){
            msg = "成功将 " + couponBalance + " B币卷兑换成 "+couponBalance*1000+" 金瓜子";
        } else{
            msg = post.getString("message");
        }
        LOGGER.info("B币卷兑换金瓜子 -- " + msg);
        biliUserData.info("B币卷兑换金瓜子 -- " + msg);
    }

}
