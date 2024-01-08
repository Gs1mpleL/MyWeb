package com.wanfeng.myweb.job.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestJob {
    @XxlJob("testHandler")
    public void testHandler(){
        System.out.println(new Date());
    }
}
