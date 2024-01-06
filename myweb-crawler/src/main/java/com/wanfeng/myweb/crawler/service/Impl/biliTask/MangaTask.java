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

@Component
public class MangaTask implements Task {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(MangaTask.class);
    @Resource
    private BiliProperties biliProperties;
    @Resource
    private BiliHttpService biliHttpUtils;

    @Override
    public void run() {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        try {
            JSONObject jsonObject = mangaClockIn(biliProperties.getPlatform());
            LOGGER.info("漫画签到 -- {}", "0".equals(jsonObject.getString("code")) ? "成功" : "今天已经签过了");
            biliUserData.info("漫画签到 -- {}", "0".equals(jsonObject.getString("code")) ? "成功" : "今天已经签过了");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error("漫画签到错误 -- " + e);
            biliUserData.info("漫画签到错误 -- " + e);
        }
    }

    /**
     * 模拟漫画app签到
     *
     * @param platform 设备标识
     */
    public JSONObject mangaClockIn(String platform) {
        String body = "platform=" + platform;
        return biliHttpUtils.postWithTotalCookie("https://manga.bilibili.com/twirp/activity.v1.Activity/ClockIn", body);
    }
}

