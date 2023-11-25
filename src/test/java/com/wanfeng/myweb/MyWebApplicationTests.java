package com.wanfeng.myweb;

import com.wanfeng.myweb.quartz.DailyJob;
import com.wanfeng.myweb.service.BiliService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyWebApplicationTests {
    @Autowired
    private BiliService biliService;
    @Test
    void test() {
        biliService.doTask("liuzhuohao123");
    }
}
