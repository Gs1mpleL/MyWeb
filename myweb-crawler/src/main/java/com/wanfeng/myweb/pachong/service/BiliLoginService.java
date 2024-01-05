package com.wanfeng.myweb.pachong.service;

public interface BiliLoginService {
    void refreshCookie() throws Exception;

    void login();

    void dailyTask(String totalCookie);
}
