package com.wanfeng.myweb.service;

import com.wanfeng.myweb.vo.PushVO;

public interface PushService {
    String pushIphone(PushVO pushVO);

    String pushMac(PushVO pushVO);
}
