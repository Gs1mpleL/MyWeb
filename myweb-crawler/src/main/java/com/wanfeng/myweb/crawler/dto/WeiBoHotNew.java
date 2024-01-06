package com.wanfeng.myweb.crawler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeiBoHotNew {
    private String word;
    private String realpos;
    private String url;
}
