package com.wanfeng.myweb;

import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.service.SystemConfigService;
import com.wanfeng.myweb.service.impl.WeiBoServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyWebApplicationTests {
    @Autowired
    private WeiBoServiceImpl weiBoService;
    @Test
    void contextLoads() throws InterruptedException {
        weiBoService.pushNews();
    }
}
