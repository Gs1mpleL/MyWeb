package com.wanfeng.myweb.crawler.service;

public interface BiliLoginService {
    void refreshCookie() throws Exception;

    void login();

    void dailyTask(String totalCookie);
}
