package com.wanfeng.myweb;

import com.wanfeng.myweb.service.impl.BiliServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MyWebApplicationTests {
    @Autowired
    private BiliServiceImpl biliService;
    @Test
    void test(){
        biliService.getReply();
    }
}
