package com.wanfeng.myweb.crawler.controller;

import com.wanfeng.myweb.common.vo.Result;
import com.wanfeng.myweb.crawler.service.BiliLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
@Api(value = "哔哩哔哩接口", tags = "哔哩哔哩接口")
@RestController
@RequestMapping("/bilibili")
public class BilibiliController {
    @Resource
    private BiliLoginService loginService;
    @PostMapping("/dailyTask")
    @ApiOperation(value = "执行每日任务", notes = "每日任务")
    @ApiImplicitParam(name = "totalCookie", value = "完整的B站Cookie")
    public Result<?> dailyTask(@RequestBody String totalCookie)  {
        String s = loginService.dailyTask(totalCookie);
        return Result.ok(s);
    }
}
