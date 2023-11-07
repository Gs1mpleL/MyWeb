package com.wanfeng.myweb.quartz;

import com.wanfeng.myweb.service.BiliService;
import com.wanfeng.myweb.service.YuanShenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class BiliQuartz {
    @Autowired
    private BiliService biliService;
    @Autowired
    private YuanShenService yuanShenService;
    @Scheduled(cron = "0 0 8 * * ? ")
    public void BiliDailyTask(){
       biliService.startBiliTask();
    }

    @Scheduled(cron = "0 0 8 * * ? ")
    public void YuanShenSignDailyTask(){yuanShenService.doSign();}
}
