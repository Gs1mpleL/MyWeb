package com.wanfeng.myweb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.service.BiliService;
import com.wanfeng.myweb.service.biliTask.CompetitionGuessTask;
import com.wanfeng.myweb.service.biliTask.DailyTask;
import com.wanfeng.myweb.service.biliTask.ThrowCoinTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyWebApplicationTests {

    @Autowired
    BiliService biliService;
    @Test
    void contextLoads() {
        biliService.biliTask();


    }

    void setContent(String message,String aid){

    }

}
