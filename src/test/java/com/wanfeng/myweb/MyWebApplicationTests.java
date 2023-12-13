package com.wanfeng.myweb;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wanfeng.myweb.Entity.QuestionEntity;
import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.config.YuanshenConfig;
import com.wanfeng.myweb.service.QuestionService;
import com.wanfeng.myweb.service.SystemConfigService;
import com.wanfeng.myweb.service.YuanShenService;
import com.wanfeng.myweb.service.impl.BiliServiceImpl;
import com.wanfeng.myweb.service.impl.biliTask.DailyTask;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class MyWebApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyWebApplicationTests.class);

    @Autowired
    QuestionService questionService;
    @Test
    void test() throws Exception {
        QueryWrapper<QuestionEntity> eq = new QueryWrapper<QuestionEntity>().eq("subject", "英语单词");
        List<QuestionEntity> list = questionService.list(eq);
        List<String> array = new ArrayList<>();
        for (QuestionEntity questionEntity : list) {
            array.add(questionEntity.getQuestion());
        }
        String fileName = "a.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String word : array) {
                if (word.contains("20")){
                    continue;
                }
                writer.write(word);
                writer.newLine(); // 每个单词写入新的一行
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
