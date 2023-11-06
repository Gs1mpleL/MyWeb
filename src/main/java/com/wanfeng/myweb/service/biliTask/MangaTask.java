package com.wanfeng.myweb.service.biliTask;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.Utils.BiliRequest;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.properties.BiliProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MangaTask implements Task {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(MangaTask.class);
    @Autowired
    private BiliProperties biliProperties;
    @Autowired
    private BiliRequest biliRequest;
    @Autowired
    private BiliUserData biliUserData;
    @Override
    public void run(){
        try{
            JSONObject jsonObject = mangaClockIn(biliProperties.getPlatform());
            LOGGER.info("漫画签到 -- {}","0".equals(jsonObject.getString("code"))?"成功":"今天已经签过了");
            biliUserData.info("漫画签到 -- {}","0".equals(jsonObject.getString("code"))?"成功":"今天已经签过了");
        } catch (Exception e){
            LOGGER.error("漫画签到错误 -- "+e);
            biliUserData.info("漫画签到错误 -- "+e);
        }
    }

    /**
     * 模拟漫画app签到
     * @param platform 设备标识
     */
    public JSONObject mangaClockIn(String platform){
        String body = "platform="+platform;
        return biliRequest.post("https://manga.bilibili.com/twirp/activity.v1.Activity/ClockIn", body);
    }
}

