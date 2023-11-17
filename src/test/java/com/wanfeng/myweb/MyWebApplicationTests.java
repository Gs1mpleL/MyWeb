package com.wanfeng.myweb;

import com.wanfeng.myweb.service.BiliService;
import com.wanfeng.myweb.service.SystemConfigService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyWebApplicationTests {
    @Autowired
    SystemConfigService systemConfigService;

    @Autowired
    BiliService biliService;
    @Test
    void contextLoads() throws Exception {
//        System.out.println(systemConfigService.getById(1).getBiliCookie());
//        biliService.doTask("liuzhuohao123");
    }
}
