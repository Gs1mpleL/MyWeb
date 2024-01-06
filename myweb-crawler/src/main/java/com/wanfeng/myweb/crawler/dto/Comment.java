package com.wanfeng.myweb.crawler.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class Comment {
    private String oid;
    private String content;
    private String replyId;

    public Comment(JSONObject data, String oid) {
        content = data.getJSONObject("content").getString("message");
        replyId = data.getString("rpid");
        this.oid = oid;
    }
}
