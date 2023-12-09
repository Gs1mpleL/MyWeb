package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.service.WeiBoService;
import com.wanfeng.myweb.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeiBoController {
    @Autowired
    WeiBoService weiBoService;

    @RequestMapping("/weibo")
    public Result<?> getHot() {
        return Result.ok(weiBoService.getHotList());
    }
}
