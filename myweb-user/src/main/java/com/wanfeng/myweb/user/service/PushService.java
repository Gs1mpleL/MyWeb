package com.wanfeng.myweb.user.service;


import com.wanfeng.myweb.api.dto.PushVO;

/**
 * 消息推送接口
 */
public interface PushService {
    boolean pushIphone(PushVO pushVO);

    boolean pushIphone(String quickMsg);

    void pushMac(PushVO pushVO);
}
