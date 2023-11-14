package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.service.YuanShenService;
import com.wanfeng.myweb.service.impl.YuanShenServiceImpl;
import com.wanfeng.myweb.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class YuanshenController {
    @Autowired
    private YuanShenService yuanShenService;
    @GetMapping("/yuanshen")
    public Result<?> yuanShenTask(){
        yuanShenService.doTask();
        return Result.ok();

    }
}
