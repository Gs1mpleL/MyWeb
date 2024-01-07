package com.wanfeng.myweb.crawler.service;

public interface BiliLoginService {
    void refreshCookie() throws Exception;

    void login();

    String dailyTask(String totalCookie);
}
