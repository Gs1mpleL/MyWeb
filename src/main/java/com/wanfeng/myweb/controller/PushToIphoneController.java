package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.Utils.HttpUtil;
import com.wanfeng.myweb.service.PushIphoneService;
import com.wanfeng.myweb.vo.PushIphoneVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Api("Iphone推送")
public class TestController {

    @Autowired
    private PushIphoneService pushIphoneService;

    @RequestMapping("/pushToIphone")
    public void test(PushIphoneVo pushIphoneVo){
        pushIphoneService.push(pushIphoneVo);
    }
}
