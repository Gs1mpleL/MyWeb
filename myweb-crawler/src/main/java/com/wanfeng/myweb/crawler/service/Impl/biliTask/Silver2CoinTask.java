package com.wanfeng.myweb.crawler.service.Impl.biliTask;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.common.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.crawler.config.BiliUserData;
import com.wanfeng.myweb.crawler.properties.BiliProperties;
import com.wanfeng.myweb.crawler.service.BiliHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 银瓜子兑换硬币
 */
@Component
public class Silver2CoinTask implements Task {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(Silver2CoinTask.class);
    @Resource
    private BiliProperties biliProperties;
    @Resource
    private BiliHttpService biliHttpUtils;

    @Override
    public void run() {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        if (biliProperties.isS2c()) {
            try {
                /* 获得银瓜子的数量 */
                Integer silver = getSilver();
                LOGGER.info("拥有银瓜子数量: {}", silver);
                biliUserData.info("拥有银瓜子数量: {}", String.valueOf(silver));
                /* 如果银瓜子数量小于700没有必要再进行兑换 */
                if (silver < 700) {
                    LOGGER.info("银瓜子兑换硬币 -- {}", "银瓜子余额不足");
                    biliUserData.info("银瓜子兑换硬币 -- {}", "银瓜子余额不足");
                } else {
                    JSONObject jsonObject = silver2coin();
                    LOGGER.warn("银瓜子兑换硬币 -- {}", jsonObject.getString("message"));
                    biliUserData.info("银瓜子兑换硬币 -- {}", jsonObject.getString("message"));
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                LOGGER.error("银瓜子兑换硬币错误 -- " + e);
                biliUserData.info("银瓜子兑换硬币错误 -- " + e);
            }
        }
    }

    /**
     * 银瓜子兑换成硬币
     *
     * @return JSONObject
     */
    public JSONObject silver2coin() {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        String body = "csrf=" + biliUserData.getBiliJct();
        return biliHttpUtils.postWithTotalCookie("https://api.live.bilibili.com/xlive/revenue/v1/wallet/silver2coin", body);
    }

    /**
     * 获取银瓜子的数量
     *
     * @return Integer
     */
    public Integer getSilver() {
        JSONObject jsonObject = biliHttpUtils.getWithTotalCookie("https://api.live.bilibili.com/xlive/revenue/v1/wallet/getStatus");
        return Integer.parseInt(jsonObject.getJSONObject("data").getString("silver"));
    }
}
