package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.service.BiliService;
import com.wanfeng.myweb.service.SystemConfigService;
import com.wanfeng.myweb.service.TestService;
import com.wanfeng.myweb.vo.BiliVo;
import com.wanfeng.myweb.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {
    @Resource
    private TestService testService;
    @Resource
    private BiliService biliService;
    @Resource
    private SystemConfigService systemConfigService;

    @PostMapping("/test")
    public Result<?> test(@RequestBody BiliVo biliVo) {
        int a = 10/0;
        return Result.ok();
    }
}
