package com.wanfeng.myweb.quartz;

import com.alibaba.fastjson.JSONArray;
import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.service.SystemConfigService;
import com.wanfeng.myweb.service.impl.BiliServiceImpl;
import com.wanfeng.myweb.service.impl.PushServiceImpl;
import com.wanfeng.myweb.service.impl.YuanShenServiceImpl;
import com.wanfeng.myweb.service.impl.biliTask.DailyTask;
import com.wanfeng.myweb.vo.PushVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class dailyJob {
    @Autowired
    private BiliServiceImpl biliService;
    @Autowired
    private YuanShenServiceImpl yuanShenService;
    @Autowired
    private PushServiceImpl pushIphoneService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private DailyTask dailyTask;
    @Scheduled(cron = "0 0 8 * * ?")
    public void BiliDailyTask() throws Exception {
       biliService.biliTask(true);
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void commentTask() throws InterruptedException {
        if ( ThreadLocalUtils.get("biliUserData", BiliUserData.class) == null){
            ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA,new BiliUserData(systemConfigService.getById(1).getBiliCookie()));
        }
        dailyTask.commentTask();
    }
}
