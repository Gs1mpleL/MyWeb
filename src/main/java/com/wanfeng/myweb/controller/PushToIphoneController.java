package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.service.PushIphoneService;
import com.wanfeng.myweb.vo.PushIphoneVo;
import com.wanfeng.myweb.vo.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("Iphone推送")
@RestController
public class PushToIphoneController {

    @Autowired
    private PushIphoneService pushIphoneService;

    @RequestMapping("/push")
    public Result<String > push(@RequestBody PushIphoneVo pushIphoneVo){
        String res = pushIphoneService.push(pushIphoneVo);
        return Result.ok(res);
    }
}
