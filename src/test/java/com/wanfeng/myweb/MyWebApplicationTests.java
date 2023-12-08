package com.wanfeng.myweb;

import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.service.SystemConfigService;
import com.wanfeng.myweb.service.impl.BiliServiceImpl;
import com.wanfeng.myweb.service.impl.biliTask.DailyTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MyWebApplicationTests {
    @Autowired
    private DailyTask dailyTask;
    @Autowired
    private SystemConfigService systemConfigService;
    @Test
    void test() throws InterruptedException {
        ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(systemConfigService.getById(1).getBiliCookie()));

        dailyTask.commentTask(true);
    }
}
