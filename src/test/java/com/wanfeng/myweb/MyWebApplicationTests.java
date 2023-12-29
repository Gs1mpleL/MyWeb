package com.wanfeng.myweb;

import com.wanfeng.myweb.Utils.HttpUtils.Requests;
import com.wanfeng.myweb.service.PushService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class MyWebApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyWebApplicationTests.class);

    @Resource
    Requests requests;
    @Resource
    PushService pushService;
    @Test
    void test() {
        System.out.println(requests.getForHtml("https://www.baidu.com", null, null));
    }
}
