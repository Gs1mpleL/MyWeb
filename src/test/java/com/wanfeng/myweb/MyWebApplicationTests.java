package com.wanfeng.myweb;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanfeng.myweb.Entity.QuestionEntity;
import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.service.QuestionService;
import com.wanfeng.myweb.service.SystemConfigService;
import com.wanfeng.myweb.service.impl.BiliServiceImpl;
import com.wanfeng.myweb.service.impl.biliTask.DailyTask;
import com.wanfeng.myweb.temp.BaiduFanYi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


@SpringBootTest
class MyWebApplicationTests {
    @Autowired
    private DailyTask dailyTask;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private QuestionService questionService;
    @Test
    void test() throws InterruptedException {
        List<QuestionEntity> list = questionService.list(new QueryWrapper<QuestionEntity>().eq("subject", "英语单词"));
        StringBuilder need = new StringBuilder();
        System.out.println(list.size() + "总单词数");
        List<QuestionEntity> needUpdateList = new ArrayList<>();
        for (QuestionEntity questionEntity : list) {
            if (Objects.equals(questionEntity.getAnswer(), "1")){
                String question = questionEntity.getQuestion();
                String ans = BaiduFanYi.getTranslateResult(question);
                questionEntity.setAnswer(ans);
                Thread.sleep(1000);
                needUpdateList.add(questionEntity);
            }

        }

        questionService.updateBatchById(needUpdateList);
    }
}
