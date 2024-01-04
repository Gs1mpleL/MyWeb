package com.wanfeng.myweb.service;

public interface BiliService {
    void dailyTask(String totalCookie);

    void refreshCookie() throws Exception;

    String login();
}
