package com.wanfeng.myweb.pachong.controller;

import com.wanfeng.myweb.api.client.UserClient;
import com.wanfeng.myweb.common.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.pachong.config.BiliUserData;
import com.wanfeng.myweb.pachong.service.BiliLoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LoginController {
    @Resource
    private BiliLoginService loginService;
    @Resource
    private UserClient userClient;
    @GetMapping("/test")
    public void test() throws Exception {
        ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(userClient.getSystemConfig()));

        loginService.dailyTask("liuzhuohao123");
    }
}
