package com.wanfeng.myweb.crawler.service.Impl.biliTask;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.common.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.crawler.config.BiliUserData;
import com.wanfeng.myweb.crawler.service.BiliHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class BiliLiveTask implements Task {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(BiliLiveTask.class);
    /** 访问成功 */
    private static final String SUCCESS = "0";
    @Resource
    private BiliHttpService biliHttpUtils;

    @Override
    public void run() {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        try {
            JSONObject json = xliveSign();
            String msg;
            /* 获取json对象的状态码code */
            if (SUCCESS.equals(json.getString("code"))) {
                msg = "获得" + json.getJSONObject("data").getString("text");
            } else {
                msg = json.getString("message");
            }
            LOGGER.info("直播签到 -- {}", msg);
            biliUserData.info("直播签到 -- {}", msg);
            /* 直播签到后等待5秒 */
            Thread.sleep(5000);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error("直播签到等待中错误 -- " + e);
            biliUserData.info("直播签到等待中错误 -- " + e);
        }
    }

    /**
     * B站直播进行签到
     */
    public JSONObject xliveSign() {
        return biliHttpUtils.getWithTotalCookie("https://api.live.bilibili.com/xlive/web-ucenter/v1/sign/DoSign");
    }

}
