package com.wanfeng.myweb.quartz;

import com.wanfeng.myweb.service.BiliService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BiliQuartz {
    @Autowired
    private BiliService biliService;
    @Scheduled(cron = "0 0 8 * * ? ")
    public void startBiliTask(){
       biliService.startBiliTask();
    }
}
