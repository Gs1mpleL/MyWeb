package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.service.impl.BarkPushService;
import com.wanfeng.myweb.vo.PushVO;
import com.wanfeng.myweb.vo.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api("Iphone推送")
@RestController
public class PushController {

    @Autowired
    private BarkPushService pushIphoneService;

    @PostMapping("/pushIphone")
    public Result<String> pushIphone(@RequestBody PushVO pushVO) {
        pushIphoneService.pushIphone(pushVO);
        return Result.ok();
    }

    @PostMapping("/pushMac")
    public Result<String> pushMac(@RequestBody PushVO pushVO) {
        pushIphoneService.pushMac(pushVO);
        return Result.ok();
    }
}
