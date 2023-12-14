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
        dailyTest();
        weiBoService.pushNews();
//        yuanShenService.doTask();
        weatherService.pushWeather();
        kaoYan();
    }

    private void dailyTest() {
        List<List<QuestionVo>> allList = new ArrayList<>();
        List<QuestionVo> list1 = questionService.getThreeRandQuestionBySubject("肖八第一套");
        List<QuestionVo> list2 = questionService.getThreeRandQuestionBySubject("肖八第二套");
        List<QuestionVo> list3 = questionService.getThreeRandQuestionBySubject("肖八第三套");
        List<QuestionVo> list4 = questionService.getThreeRandQuestionBySubject("肖八第四套");
        List<QuestionVo> list5 = questionService.getThreeRandQuestionBySubject("肖八第五套");
        List<QuestionVo> list6 = questionService.getThreeRandQuestionBySubject("肖八第六套");
        List<QuestionVo> list7 = questionService.getThreeRandQuestionBySubject("肖八第七套");
        List<QuestionVo> list8 = questionService.getThreeRandQuestionBySubject("肖八第八套");
        List<QuestionVo> list9 = questionService.getThreeRandQuestionBySubject("肖四第一套");
        List<QuestionVo> list10 = questionService.getThreeRandQuestionBySubject("肖四第二套");
        List<QuestionVo> list11 = questionService.getThreeRandQuestionBySubject("肖四第三套");
        List<QuestionVo> list12 = questionService.getThreeRandQuestionBySubject("肖四第四套");
        allList.add(list1);
        allList.add(list2);
        allList.add(list3);
        allList.add(list4);
        allList.add(list5);
        allList.add(list6);
        allList.add(list7);
        allList.add(list8);
        allList.add(list9);
        allList.add(list10);
        allList.add(list11);
        allList.add(list12);
        StringBuilder sb = new StringBuilder();
        for (List<QuestionVo> list : allList) {
            if (!list.isEmpty()){
                for (QuestionVo questionVo : list) {
                    sb.append(questionVo.getQuestion()).append("\n");
                }
            }
        }
        pushService.pushIphone(new PushVO("测试",sb.toString(),"测试"));
    }


    @Scheduled(cron = "0 0 14 * * ?")
    public void afternoonTask() {
        kaoYan();
        biliService.refreshCookie();
        biliService.biliTask(true);
        dailyTest();
    }

    @Scheduled(cron = "0 0 20 * * ?")
    public void nightTask(){
        biliService.refreshCookie();
        dailyTask.commentTask();
        dailyTest();
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
