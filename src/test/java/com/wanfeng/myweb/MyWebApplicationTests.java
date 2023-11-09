package com.wanfeng.myweb;

import com.wanfeng.myweb.service.impl.BiliServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyWebApplicationTests {

    @Autowired
    BiliServiceImpl biliService;
    @Test
    void contextLoads() {
        biliService.biliTask();


    }

    void setContent(String message,String aid){

    }

}
