package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.service.PushIphoneService;
import com.wanfeng.myweb.vo.PushIphoneVo;
import com.wanfeng.myweb.vo.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("Iphone推送")
@RestController
public class PushToIphoneController {

    @Autowired
    private PushIphoneService pushIphoneService;

    @RequestMapping("/pushToIphone")
    public Result<String > test(PushIphoneVo pushIphoneVo){
        String res = pushIphoneService.push(pushIphoneVo);
        return Result.ok(res);
    }
}
