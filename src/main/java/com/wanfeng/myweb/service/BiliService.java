package com.wanfeng.myweb.service;

public interface BiliService {
    void DailyTaskStart(String totalCookie);

    void refreshCookie() throws Exception;

    String login();
}
