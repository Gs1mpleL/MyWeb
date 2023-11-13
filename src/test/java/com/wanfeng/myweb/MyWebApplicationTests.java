package com.wanfeng.myweb;

import com.wanfeng.myweb.service.YuanShenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyWebApplicationTests {
    @Autowired
    YuanShenService yuanShenService;

    @Test
    void contextLoads() throws Exception {
        yuanShenService.doTask();
    }
}
