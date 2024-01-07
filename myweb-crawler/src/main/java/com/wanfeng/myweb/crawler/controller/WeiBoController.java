package com.wanfeng.myweb.crawler.controller;


import com.wanfeng.myweb.common.vo.Result;
import com.wanfeng.myweb.crawler.service.WeiBoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/weibo")
public class WeiBoController {
    @Resource
    WeiBoService weiBoService;

    @RequestMapping("/news")
    public Result<?> getHot() {
        return Result.ok(weiBoService.getHotList());
    }
}
