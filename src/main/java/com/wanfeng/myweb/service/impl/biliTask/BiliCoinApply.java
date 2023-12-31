package com.wanfeng.myweb.service.impl.biliTask;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.Utils.HttpUtils.BiliHttpUtils;
import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.properties.BiliProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.TimeZone;

@Component
public class BiliCoinApply implements Task {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(BiliCoinApply.class);
    /** 28号代表月底 */
    private static final int END_OF_MONTH = 28;
    /** 代表获取到正确的json对象 code */
    private static final String SUCCESS = "0";
    /** 获取DATA对象 */
    @Resource
    private BiliHttpUtils biliHttpUtils;
    @Resource
    private BiliProperties biliProperties;

    @Override
    public void run() {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            int day = cal.get(Calendar.DATE);
            /* B币券余额 */
            int couponBalance = Integer.parseInt(biliUserData.getCouponBalance());
            if (day == END_OF_MONTH && couponBalance > 0) {
                switch (biliProperties.getAutoBiCoin()) {
                    case "1":
                        doCharge(couponBalance);
                        break;
                    case "2":
                        doMelonSeed(couponBalance);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error("使用B币卷部分异常 -- " + e);
            biliUserData.info("使用B币卷部分异常 -- " + e);
        }
    }

    /**
     * 月底自动给自己充电。仅充会到期的B币券，低于2的时候不会充
     */
    public void doCharge(int couponBalance) {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        String body = "pay_bp=" + couponBalance * 1000
                + "&context_id=" + 12299272
                + "&context_type=11"
                + "&goods_id=1"
                + "&goods_num=" + couponBalance
                + "&goods_type=2"
                + "&platform=pc"
                + "&ios_bp=0"
                + "&common_bp=" + couponBalance * 1000
                + "&csrf_token=" + biliUserData.getBiliJct()
                + "&visit_id=1zn218g8bp8g"
                + "&csrf=" + biliUserData.getBiliJct();

        JSONObject jsonObject = biliHttpUtils.postWithTotalCookie("https://api.live.bilibili.com/xlive/revenue/v1/order/createOrder", body);

        Integer resultCode = jsonObject.getInteger("code");
        if (resultCode == 0) {
            JSONObject dataJson = jsonObject.getJSONObject("data");
            LOGGER.debug(dataJson.toString());
            Integer statusCode = dataJson.getInteger("status");
            if (statusCode == 2) {
                LOGGER.info("本次给自己充值了: " + couponBalance * 10 + "个电池");
                biliUserData.info("本次给自己充值了: 「" + couponBalance * 10 + "」个电池");
                /* 获取充电留言token */
                String orderNo = dataJson.getString("order_id");
                chargeComments(orderNo);
            } else {
                LOGGER.warn("充电失败 -- " + dataJson.getString("error_info"));
                biliUserData.info("充电失败 -- {}", dataJson.getString("error_info"));
            }

        } else {
            LOGGER.warn("充电失败了啊 -- " + jsonObject);
            biliUserData.info("充电失败 {}", jsonObject.toString());
        }
    }

    /**
     * 自动充电完，添加一条评论
     *
     * @param token 订单id
     */
    public void chargeComments(String token) {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        String requestBody = "order_id=" + token
                + "&message=" + "BilibiliTask自动充电"
                + "&csrf=" + biliUserData.getBiliJct();
        JSONObject jsonObject = biliHttpUtils.postWithTotalCookie("http://api.bilibili.com/x/ugcpay/trade/elec/message", requestBody);
        LOGGER.debug(jsonObject.toString());
        biliUserData.info("充电评论:{}", jsonObject.toString());
    }

    /**
     * 用B币卷兑换成金瓜子
     *
     * @param couponBalance 传入B币卷的数量
     */
    public void doMelonSeed(Integer couponBalance) {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        String body = "platform=pc"
                + "&pay_bp=" + couponBalance * 1000
                + "&context_id=1"
                + "&context_type=11"
                + "&goods_id=1"
                + "&goods_num=" + couponBalance
                + "&csrf=" + biliUserData.getBiliJct();
        JSONObject post = biliHttpUtils.postWithTotalCookie("https://api.live.bilibili.com/xlive/revenue/v1/order/createOrder", body);
        String msg;
        /* json对象的状态码 */
        String code = post.getString("code");
        if (SUCCESS.equals(code)) {
            msg = "成功将 " + couponBalance + " B币卷兑换成 " + couponBalance * 1000 + " 金瓜子";
        } else {
            msg = post.getString("message");
        }
        LOGGER.info("B币卷兑换金瓜子 -- " + msg);
        biliUserData.info("B币卷兑换金瓜子 -- " + msg);
    }

}
