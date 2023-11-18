package com.wanfeng.myweb.quartz;

import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.service.SystemConfigService;
import com.wanfeng.myweb.service.WeiBoService;
import com.wanfeng.myweb.service.impl.BiliServiceImpl;
import com.wanfeng.myweb.service.impl.WeiBoServiceImpl;
import com.wanfeng.myweb.service.impl.biliTask.DailyTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jws.Oneway;


@Component
public class dailyJob {
    @Autowired
    @Qualifier("comment_thread")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private BiliServiceImpl biliService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private DailyTask dailyTask;
    @Autowired
    private WeiBoService weiBoService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void BiliDailyTask() throws Exception {
        biliService.biliTask(true);
        weiBoService.pushNews();
    }

    /**
     * 启动后开始执行
     */
    @PostConstruct
    public void doComment() {
        threadPoolTaskExecutor.execute(() -> {
            if (ThreadLocalUtils.get("biliUserData", BiliUserData.class) == null) {
                ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(systemConfigService.getById(1).getBiliCookie()));
            }
            while (true) {
                try {
                    dailyTask.commentTask();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
