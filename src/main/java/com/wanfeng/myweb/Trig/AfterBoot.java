package com.wanfeng.myweb.Trig;

import com.wanfeng.myweb.config.SystemConfig;
import com.wanfeng.myweb.service.impl.PushServiceImpl;
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
    private PushServiceImpl pushIphoneService;
    @Autowired
    private SystemConfig systemConfig;

    @Override
    public void run(String... args) throws Exception {
        if (systemConfig.getBootAlarm() == 1) {
            pushIphoneService.pushIphone(new PushVO("MyWeb", "项目启动成功", "MyWeb"));
        }
    }
}
