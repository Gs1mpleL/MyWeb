package com.wanfeng.myweb;

import com.wanfeng.myweb.quartz.DailyJob;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyWebApplicationTests {
    @Autowired
    private DailyJob dailyJob;
    @Test
    void test() throws Exception {

    }
}
