package com.wanfeng.myweb;

import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.service.BiliService;
import com.wanfeng.myweb.service.SystemConfigService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class MyWebApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyWebApplicationTests.class);

    @Resource
    private BiliService biliService;
    @Resource
    private SystemConfigService systemConfigService;

    @Test
    void test() {
        ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(systemConfigService.getById(1)));

        biliService.login();
    }
}
