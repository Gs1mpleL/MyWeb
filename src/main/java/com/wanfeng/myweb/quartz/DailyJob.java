package com.wanfeng.myweb.quartz;

import com.wanfeng.myweb.service.*;
import com.wanfeng.myweb.service.impl.BiliServiceImpl;
import com.wanfeng.myweb.service.impl.biliTask.BiliDailyTask;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class DailyJob {
    @Resource
    private BiliServiceImpl biliService;
    @Resource
    private BiliDailyTask biliDailyTask;
    @Resource
    private WeiBoService weiBoService;

    @Resource
    private WeatherService weatherService;

    @Resource
    private PushService pushService;

    @Resource
    private SystemConfigService systemConfigService;
    @Resource
    private QuestionService questionService;

    @Scheduled(cron = "0 0 7 * * ?")
    public void morningTask() throws Exception {
        weiBoService.pushNews();
        weatherService.pushWeather();
    }


    @Scheduled(cron = "0 0 14 * * ?")
    public void afternoonTask() throws Exception {
        biliService.refreshCookie();
        biliService.biliTask(true);
    }

    @Scheduled(cron = "0 0 22 * * ?")
    public void nightTask() throws Exception {
        biliService.refreshCookie();
        biliDailyTask.commentTask();
    }

}
