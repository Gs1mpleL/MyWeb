package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.service.YuanShenService;
import com.wanfeng.myweb.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class YuanshenController {
    @Resource
    private YuanShenService yuanShenService;

    @GetMapping("/yuanshen")
    public Result<?> yuanShenTask() {
        yuanShenService.signTaskStart();
        return Result.ok();
    }
}
