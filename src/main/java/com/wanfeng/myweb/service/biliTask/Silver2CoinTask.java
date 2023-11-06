package com.wanfeng.myweb.service.biliTask;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.Utils.BiliRequest;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.properties.BiliProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 银瓜子兑换硬币
 * 
 */
@Component
public class Silver2CoinTask implements Task {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(Silver2CoinTask.class);
    @Autowired
    private BiliUserData biliUserData;
    @Autowired
    private BiliProperties biliProperties;
    @Autowired
    private BiliRequest biliRequest;
    @Override
    public void run(){
        if(biliProperties.isS2c()){
            try{
                /* 获得银瓜子的数量 */
                Integer silver = getSilver();
                LOGGER.info("银瓜子: {}",silver);
                biliUserData.info("银瓜子: {}", String.valueOf(silver));
                /* 如果银瓜子数量小于700没有必要再进行兑换 */
                if(silver<700){
                    LOGGER.info("银瓜子兑换硬币 -- {}","银瓜子余额不足");
                    biliUserData.info("银瓜子兑换硬币 -- {}","银瓜子余额不足");
                } else{
                    LOGGER.warn("银瓜子兑换硬币 -- {}",silver2coin().getString("msg"));
                    biliUserData.info("银瓜子兑换硬币 -- {}",silver2coin().getString("msg"));
                }
            } catch (Exception e){
                LOGGER.error("银瓜子兑换硬币错误 -- "+e);
                biliUserData.info("银瓜子兑换硬币错误 -- "+e);
            }
        }
    }

    /**
     * 银瓜子兑换成硬币
     * @return JSONObject
     */
    public JSONObject silver2coin(){
        String body = "csrf="+ biliProperties.getBiliJct();
        return biliRequest.post("https://api.live.bilibili.com/pay/v1/Exchange/silver2coin", body);
    }

    /**
     * 获取银瓜子的数量
     * @return Integer
     */
    public Integer getSilver(){
        JSONObject jsonObject = biliRequest.get("https://api.live.bilibili.com/xlive/web-ucenter/user/get_user_info");
        return Integer.parseInt(jsonObject.getJSONObject("data").getString("silver"));
    }
}
