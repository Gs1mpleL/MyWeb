package com.wanfeng.myweb.user.controller;

import com.wanfeng.myweb.api.dto.PushVO;
import com.wanfeng.myweb.user.service.PushService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class PushController {
    @Resource
    private PushService pushService;
    @PostMapping("/pushIphone")
    public boolean pushIphone(@RequestBody PushVO pushVO){
        return pushService.pushIphone(pushVO);
    }
    @PostMapping("/pushIphoneEasy")
    public boolean pushIphoneEasy(@RequestBody String msg){
        return pushService.pushIphone(msg);
    }
}
