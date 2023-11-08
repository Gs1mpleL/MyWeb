package com.wanfeng.myweb.Trig;

import com.wanfeng.myweb.service.PushIphoneService;
import com.wanfeng.myweb.vo.PushVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 项目启动后调用
 */
@Component
public class AfterBoot implements CommandLineRunner {
    @Autowired
    private PushIphoneService pushIphoneService;
    @Override
    public void run(String... args) throws Exception {
        pushIphoneService.pushIphone(new PushVO("MyWeb","项目启动成功","MyWeb"));
    }
}
