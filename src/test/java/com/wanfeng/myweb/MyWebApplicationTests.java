package com.wanfeng.myweb;

import com.wanfeng.myweb.service.impl.WeatherServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyWebApplicationTests {
    @Autowired
    private WeatherServiceImpl weatherService;
    @Test
    void test(){
        weatherService.pushWeather();
    }
}
