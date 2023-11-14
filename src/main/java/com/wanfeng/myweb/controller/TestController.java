package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.service.TestService;
import com.wanfeng.myweb.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public Result<?> test(){
        return Result.ok(testService.list());
    }
}
