package com.wanfeng.myweb.pachong.service;

import com.alibaba.fastjson.JSONObject;

public interface BiliHttpService {
    JSONObject getWithTotalCookie(String url);

    JSONObject getForUpdateCookie(String url);

    String getHtml(String url);

    JSONObject postWithTotalCookie(String url, String body);

    JSONObject postForUpdateCookie(String url, String body);
}
