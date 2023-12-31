package com.wanfeng.myweb.service;

public interface BiliService {
    void dailyTask(String totalCookie);

    boolean setComment(String oid,String msg);
    void refreshCookie() throws Exception;

    String login();
}
