package com.wanfeng.myweb.Utils.HttpUtils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class Requests {
    private static final Logger LOGGER = LoggerFactory.getLogger(Requests.class);
    @Resource
    private RestTemplate restTemplate;

    public JSONObject get(String url, Map<String, String> map, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
        if (headers != null) {
            headers.forEach(httpHeaders::add);
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        url = addParamToUrl(url, map);
        LOGGER.warn("发送请求->[{}],参数->[{}],Header->[{}]", url, map, httpHeaders);
        ResponseEntity<JSONObject> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, JSONObject.class);
        LOGGER.warn("结果->[{}]", response);
        return response.getBody();
    }

    public JSONObject post(String url, Map<String, String> map, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::add);
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(null, httpHeaders);
        url = addParamToUrl(url, map);
        LOGGER.warn("发送请求->[{}],参数->[{}],Header->[{}]", url, map, httpHeaders);
        ResponseEntity<JSONObject> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JSONObject.class);
        LOGGER.warn("结果->[{}]", response);
        return response.getBody();
    }

    public String getForHtml(String url, Map<String, String> map, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::add);
        }
        url = addParamToUrl(url, map);
        LOGGER.warn("发送请求->[{}],参数->[{}],Header->[{}]", url, map, httpHeaders);
        String html = restTemplate.getForObject(url, String.class, httpHeaders);
        LOGGER.warn("结果->[{}]", html);
        return html;
    }

    /****************************************实现结束***************************************************************/

    public String addParamToUrl(String url, Map<String, String> params) {
        // 合并params到url上，如果存在的话
        if (params != null && !params.isEmpty()) {
            url += "?";
            StringBuilder urlBuilder = new StringBuilder(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            url = urlBuilder.toString();
            url = url.substring(0, url.length() - 1); // 去掉最后的"&"
        }
        return url;
    }
}