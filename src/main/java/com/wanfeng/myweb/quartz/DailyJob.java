package com.wanfeng.myweb.quartz;

import com.wanfeng.myweb.service.PushService;
import com.wanfeng.myweb.service.WeatherService;
import com.wanfeng.myweb.service.WeiBoService;
import com.wanfeng.myweb.service.YuanShenService;
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
    @Scheduled(cron = "0 0 6 * * ?")
    public void dailyTask() throws Exception {
        biliService.biliTask(true);
        weiBoService.pushNews();
        yuanShenService.doTask();
        weatherService.pushWeather();
        dailyTask.commentTask();
    }

    @Scheduled(cron = "0 30 7 * * ?")
    public void noticeTask1(){
        // 目标日期
        LocalDate targetDate = LocalDate.of(2023, 12, 23);
        // 当前日期
        LocalDate currentDate = LocalDate.now();
        // 计算剩余天数
        long daysUntil = ChronoUnit.DAYS.between(currentDate, targetDate);
        if (daysUntil < 0) {
            daysUntil = -daysUntil; // 如果目标日期在当前日期之前，那么返回的就是正数天数了。
        }
        pushService.pushIphone(new PushVO("你已经坚持了2年了,最后还剩" + daysUntil+"天了，再坚持一下吧！"));
    }

    @Scheduled(cron = "0 30 13 * * ?")
    public void noticeTask2(){
        // 目标日期
        LocalDate targetDate = LocalDate.of(2023, 12, 23);
        // 当前日期
        LocalDate currentDate = LocalDate.now();
        // 计算剩余天数
        long daysUntil = ChronoUnit.DAYS.between(currentDate, targetDate);
        if (daysUntil < 0) {
            daysUntil = -daysUntil; // 如果目标日期在当前日期之前，那么返回的就是正数天数了。
        }
        pushService.pushIphone(new PushVO("你已经坚持了2年了,最后还剩" + daysUntil+"天了，再坚持一下吧！"));
    }
}
