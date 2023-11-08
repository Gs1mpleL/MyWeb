package com.wanfeng.myweb.quartz;

import com.wanfeng.myweb.service.BiliService;
import com.wanfeng.myweb.service.PushIphoneService;
import com.wanfeng.myweb.service.YuanShenService;
import com.wanfeng.myweb.vo.PushIphoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class dailyJob {
    @Autowired
    private BiliService biliService;
    @Autowired
    private YuanShenService yuanShenService;
    @Autowired
    private PushIphoneService pushIphoneService;
    @Scheduled(cron = "0 0 8 * * ? ")
    public void BiliDailyTask(){
       biliService.startBiliTask();
    }

    @Scheduled(cron = "0 0 8 * * ? ")
    public void YuanShenSignDailyTask(){
//        yuanShenService.doSign();
        pushIphoneService.push(new PushIphoneVo("原神","未签到等待风控结束测试","原神"));
    }
}
