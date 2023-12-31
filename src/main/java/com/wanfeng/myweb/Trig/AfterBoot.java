package com.wanfeng.myweb.Trig;

import com.wanfeng.myweb.config.SystemConfig;
import com.wanfeng.myweb.service.impl.BarkPushService;
import com.wanfeng.myweb.vo.PushVO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目启动后调用
 */
@Component
public class AfterBoot implements CommandLineRunner {
    @Resource
    private BarkPushService barkPushService;
    @Resource
    private SystemConfig systemConfig;

    @Override
    public void run(String... args) throws Exception {
        if (systemConfig.getBootAlarm() == 1) {
            barkPushService.pushIphone(new PushVO("MyWeb", "项目启动成功", "MyWeb"));
        }
    }
}
