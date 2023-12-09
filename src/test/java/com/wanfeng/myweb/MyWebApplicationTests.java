package com.wanfeng.myweb;

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


@SpringBootTest
class MyWebApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyWebApplicationTests.class);

    @Autowired
    DailyTask dailyTask;
    @Test
    void test() throws Exception {
        dailyTask.commentTask();
    }
}
