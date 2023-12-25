package com.wanfeng.myweb.service;

import com.wanfeng.myweb.vo.PushVO;

/**
 * 消息推送接口
 */
public interface PushService {
    boolean pushIphone(PushVO pushVO);
    boolean pushIphone(String quickMsg);
    void pushMac(PushVO pushVO);
}
