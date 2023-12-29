package com.wanfeng.myweb.quartz;

import com.wanfeng.myweb.service.*;
import com.wanfeng.myweb.service.impl.BiliServiceImpl;
import com.wanfeng.myweb.service.impl.biliTask.DailyTask;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class DailyJob {
    @Resource
    private BiliServiceImpl biliService;
    @Resource
    private DailyTask dailyTask;
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
    public void afternoonTask() {
        biliService.refreshCookie();
        biliService.biliTask(true);
    }

    @Scheduled(cron = "0 0 20 * * ?")
    public void nightTask(){
        biliService.refreshCookie();
        dailyTask.commentTask();
    }

}
