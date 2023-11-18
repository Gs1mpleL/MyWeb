package com.wanfeng.myweb.po;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeiBoHotNew {
    private String word;
    private String realpos;
    private String url;
}
