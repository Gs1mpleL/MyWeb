package com.wanfeng.myweb.crawler.service;



import com.wanfeng.myweb.crawler.dto.WeiBoHotNew;

import java.util.ArrayList;

public interface WeiBoService {
    void pushNews();

    ArrayList<WeiBoHotNew> getHotList();
}
