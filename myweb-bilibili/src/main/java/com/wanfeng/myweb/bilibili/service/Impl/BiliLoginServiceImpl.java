package com.wanfeng.myweb.bilibili.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.common.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.bilibili.config.BiliUserData;
import com.wanfeng.myweb.bilibili.service.BiliLoginService;
import com.wanfeng.myweb.bilibili.service.SystemConfigService;
import com.wanfeng.myweb.bilibili.utils.BiliHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BiliLoginServiceImpl implements BiliLoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiliLoginServiceImpl.class);
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private BiliHttpUtils biliHttpUtils;
    @Override
    public void refreshCookie() throws Exception {
        ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(systemConfigService.getById(1)));
        JSONObject isNeedRefresh = biliHttpUtils.getWithTotalCookie("https://passport.bilibili.com/x/passport-login/web/cookie/info");
        LOGGER.info("开始检查是否需要刷新Cookie:[{}]", isNeedRefresh);
        if (isNeedRefresh.getJSONObject("data").getString("refresh").equals("true")) {
            LOGGER.info("需要刷新，开始刷新！");
            doRefresh();
        } else {
            LOGGER.info("不需要刷新！");
        }
    }

    @Override
    public String login() {
        return null;
    }
}
