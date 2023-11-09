package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.config.BiliData;
import com.wanfeng.myweb.service.BiliService;
import com.wanfeng.myweb.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class BiliController {
    @Autowired
    private BiliService biliService;
    @Autowired
    private BiliData biliData;
    @GetMapping("/biliTask")
    public Result<String> biliTask(){
        biliService.biliTask();
        String sendMsg = biliData.getSendMsg();
        biliData.setSendMsg("");
        return Result.ok(sendMsg);
    }
}
