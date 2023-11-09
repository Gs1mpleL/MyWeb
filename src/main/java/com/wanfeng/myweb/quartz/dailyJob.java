package com.wanfeng.myweb.quartz;

import com.wanfeng.myweb.service.impl.BiliServiceImpl;
import com.wanfeng.myweb.service.impl.PushServiceImpl;
import com.wanfeng.myweb.service.impl.YuanShenServiceImpl;
import com.wanfeng.myweb.vo.PushVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class dailyJob {
    @Autowired
    private BiliServiceImpl biliService;
    @Autowired
    private YuanShenServiceImpl yuanShenService;
    @Autowired
    private PushServiceImpl pushIphoneService;
    @Scheduled(cron = "0 0 8 * * ? ")
    public void BiliDailyTask(){
       biliService.biliTask();
    }

    @Scheduled(cron = "0 0 8 * * ? ")
    public void YuanShenSignDailyTask(){
//        yuanShenService.doSign();
        pushIphoneService.pushIphone(new PushVO("原神","未签到等待风控结束测试","原神"));
    }
}
