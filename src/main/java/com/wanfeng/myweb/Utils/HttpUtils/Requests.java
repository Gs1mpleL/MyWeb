package com.wanfeng.myweb.Utils.HttpUtils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class Requests {
    private static final Logger LOGGER = LoggerFactory.getLogger(Requests.class);
    @Autowired
    private RestTemplate restTemplate;

    public JSONObject get(String url, Map<String, String> map, Map<String,String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
        if (headers!=null){
            headers.forEach(httpHeaders::add);
        }
        HttpEntity<String> paramAndHeaderEntity = new HttpEntity<>(generateRequestParameters(url, map), httpHeaders);
        ResponseEntity<JSONObject> response = restTemplate.getForEntity(url, JSONObject.class, paramAndHeaderEntity);
        LOGGER.warn("发送请求->[{}],参数->[{}],Header->[{}],结果->[{}]",url,map,headers,response);
        return  response.getBody();
    }

    public JSONObject post(String url, Map<String, String> map,Map<String,String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8");
        if (headers!=null){
            headers.forEach(httpHeaders::add);
        }
        HttpEntity<String> paramAndHeaderEntity = new HttpEntity<>(JSONObject.toJSONString(map),httpHeaders);
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, paramAndHeaderEntity,JSONObject.class);
        LOGGER.warn("发送请求->[{}],参数->[{}],Header->[{}],结果->[{}]",url,map,headers,response);
        return  response.getBody();
    }

    public String getForHtml(String url, Map<String, String> map, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers!=null){
            headers.forEach(httpHeaders::add);
        }
        HttpEntity<String> paramAndHeaderEntity = new HttpEntity<>(generateRequestParameters(url, map), httpHeaders);
        String html = restTemplate.getForObject(url, String.class, paramAndHeaderEntity);
        LOGGER.warn("发送请求->[{}],参数->[{}],Header->[{}],结果->[{}]",url,map,headers,html);
        return html;
    }

/****************************************实现结束***************************************************************/

    public String generateRequestParameters(String uri, Map<String, String> params) {
        if (params == null) {
            return uri;
        }
        StringBuilder sb = new StringBuilder(uri);
        sb.append("?");
        for (Map.Entry<String, String> map : params.entrySet()) {
            sb.append(map.getKey())
                    .append("=")
                    .append(map.getValue())
                    .append("&");
        }
        uri = sb.substring(0, sb.length() - 1);
        return uri;
    }
}