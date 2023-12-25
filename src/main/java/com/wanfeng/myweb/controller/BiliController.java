package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.config.BizException;
import com.wanfeng.myweb.service.BiliService;
import com.wanfeng.myweb.vo.BiliVo;
import com.wanfeng.myweb.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BiliController {
    @Autowired
    private BiliService biliService;

    @PostMapping("/biliTask")
    public Result<String> biliTask(@RequestBody BiliVo biliVo) {
        if (biliVo == null || biliVo.getTotalCookie().equals("")) {
            throw new BizException("totalCookie为空");
        }
        biliService.DailyTaskStart(biliVo.getTotalCookie());
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        String sendMsg = biliUserData.getSendMsg() == null ? "" : biliUserData.getSendMsg();
        return Result.ok(sendMsg);
    }
}
