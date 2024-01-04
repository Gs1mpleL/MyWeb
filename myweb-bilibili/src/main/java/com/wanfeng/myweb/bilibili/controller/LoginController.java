package com.wanfeng.myweb.bilibili.controller;

import com.wanfeng.common.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.api.client.UserClient;
import com.wanfeng.myweb.bilibili.config.BiliUserData;
import com.wanfeng.myweb.bilibili.service.BiliLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private BiliLoginService loginService;
    @Autowired
    private UserClient userClient;
    @GetMapping("/test")
    public void test() throws Exception {
        ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(userClient.getSystemConfig()));

        loginService.refreshCookie();
    }

    @GetMapping("/test22")
    public void test2() throws Exception {
        ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(userClient.getSystemConfig()));
        loginService.login();
    }
}
