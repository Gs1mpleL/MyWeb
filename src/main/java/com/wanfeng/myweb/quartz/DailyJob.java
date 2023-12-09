package com.wanfeng.myweb.quartz;

import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.service.*;
import com.wanfeng.myweb.service.impl.BiliServiceImpl;
import com.wanfeng.myweb.service.impl.biliTask.DailyTask;
import com.wanfeng.myweb.vo.PushVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Component
public class DailyJob {
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

    @Autowired
    private PushService pushService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Scheduled(cron = "0 0 7 * * ?")
    public void morningTask() throws Exception {
        weiBoService.pushNews();
//        yuanShenService.doTask();
        weatherService.pushWeather();
        kaoYan();
    }


    @Scheduled(cron = "0 0 14 * * ?")
    public void afternoonTask() {
        kaoYan();
        biliService.refreshCookie();
        biliService.biliTask(true);
    }

    @Scheduled(cron = "0 0 23 * * ?")
    public void nightTask() throws InterruptedException {
        dailyTask.commentTask();
    }

    //TODO:考完就删！！！
    private void kaoYan() {
        // 目标日期
        LocalDate targetDate = LocalDate.of(2023, 12, 23);
        // 当前日期
        LocalDate currentDate = LocalDate.now();
        // 计算剩余天数
        long daysUntil = ChronoUnit.DAYS.between(currentDate, targetDate);
        if (daysUntil < 0) {
            daysUntil = -daysUntil; // 如果目标日期在当前日期之前，那么返回的就是正数天数了。
        }
        pushService.pushIphone(new PushVO("你已经坚持了2年了,最后还剩" + daysUntil + "天了，再坚持一下吧！"));
    }

}
