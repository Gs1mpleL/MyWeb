package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.service.BiliService;
import com.wanfeng.myweb.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class BiliController {
    @Autowired
    private BiliService biliService;
    @Autowired
    private BiliUserData biliUserData;
    @GetMapping("/biliTask")
    public Result<String> biliTask(){
        biliService.startBiliTask();
        return Result.ok(biliUserData.getSendMsg());
    }
}
