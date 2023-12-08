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
        ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(systemConfigService.getById(1).getBiliCookie()));

        dailyTask.commentTask(false);
    }
}
