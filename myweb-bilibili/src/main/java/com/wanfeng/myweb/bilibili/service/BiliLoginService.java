package com.wanfeng.myweb.bilibili.service;

public interface BiliLoginService {
    void refreshCookie() throws Exception;

    String login();
}
