package com.wanfeng.myweb.service;

import com.wanfeng.myweb.po.WeiBoHotNew;

import java.util.ArrayList;

public interface WeiBoService {
    void pushNews();

    ArrayList<WeiBoHotNew> getHotList();
}
