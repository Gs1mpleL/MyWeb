package com.wanfeng.myweb.temp;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BaiduFanYi {
    // 对接的api为百度翻译
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private static final String appid = "20210801000903395";
    private static final String securityKey = "j7qDDoErQpwLUqoelwgw";

    // 发送查询
    public static String getTranslateResult(String query) {
        Map<String, Object> params = new HashMap();
        params.put("q", query);
        params.put("from", "auto");
        params.put("to", "zh");
        params.put("appid", appid);
        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        // 签名
        String src = appid + query + salt + securityKey;
        // 加密前的原文
        params.put("sign", SecureUtil.md5(src));
        JSONObject jsonObject = JSONObject.parseObject(HttpUtil.get(TRANS_API_HOST, params));
        return jsonObject.getJSONArray("trans_result").getJSONObject(0).getString("dst");
    }
}
