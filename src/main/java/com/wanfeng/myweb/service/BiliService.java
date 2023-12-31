package com.wanfeng.myweb.service;

import com.wanfeng.myweb.dto.Comment;

import java.util.List;

public interface BiliService {
    void dailyTask(String totalCookie);

    void refreshCookie() throws Exception;

    String login();
}
