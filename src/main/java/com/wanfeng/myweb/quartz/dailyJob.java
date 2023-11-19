package com.wanfeng.myweb.quartz;

import com.wanfeng.myweb.service.WeatherService;
import com.wanfeng.myweb.service.WeiBoService;
import com.wanfeng.myweb.service.YuanShenService;
import com.wanfeng.myweb.service.impl.BiliServiceImpl;
import com.wanfeng.myweb.service.impl.biliTask.DailyTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class dailyJob {
    @Autowired
    private BiliServiceImpl biliService;
    @Autowired
    private DailyTask dailyTask;
    @Autowired
    private WeiBoService weiBoService;
    @Autowired
    private YuanShenService yuanShenService;

    @Autowired
    private WeatherService weatherService;

    @Scheduled(cron = "0 0 7 * * ?")
    public void BiliDailyTask() throws Exception {
        biliService.biliTask(true);
        weiBoService.pushNews();
        yuanShenService.doTask();
        weatherService.pushWeather();
        dailyTask.commentTask();
    }
}
