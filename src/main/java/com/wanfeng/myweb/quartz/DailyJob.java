package com.wanfeng.myweb.quartz;

import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.service.*;
import com.wanfeng.myweb.service.impl.BiliServiceImpl;
import com.wanfeng.myweb.service.impl.biliTask.DailyTask;
import com.wanfeng.myweb.vo.PushVO;
import com.wanfeng.myweb.vo.QuestionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Component
public class DailyJob {
    @Autowired
    private BiliServiceImpl biliService;
    @Autowired
    private DailyTask dailyTask;
    @Autowired
    private WeiBoService weiBoService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private PushService pushService;

    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
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
