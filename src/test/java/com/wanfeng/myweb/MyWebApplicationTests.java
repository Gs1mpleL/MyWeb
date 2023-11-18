package com.wanfeng.myweb;

import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.service.SystemConfigService;
import com.wanfeng.myweb.service.impl.biliTask.DailyTask;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class MyWebApplicationTests {
    @Autowired
    SystemConfigService systemConfigService;

    @Autowired
    DailyTask dailyTask;
    @Test
    void contextLoads(){
        ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(systemConfigService.getById(1).getBiliCookie()));
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            try {
                dailyTask.getRegions("1",String.valueOf(i));
            }catch (Exception e){
                continue;
            }
            list.add(i);
        }
        System.out.println(list);
    }
}
