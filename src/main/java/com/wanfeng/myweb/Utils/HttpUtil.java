package com.wanfeng.myweb.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class HttpUtil {

    @Autowired
    private RestTemplate restTemplate;

    public HttpEntity<Map<String, String>> generatePostJson(Map<String, String> jsonMap) {

        HttpHeaders httpHeaders = new HttpHeaders();

        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");

        httpHeaders.setContentType(type);

        return new HttpEntity<>(jsonMap, httpHeaders);
    }


    public String generateRequestParameters(String protocol, String uri, Map<String, String> params) {
        // StringBuilder sb = new StringBuilder(protocol).append("://").append(uri);
        if (params == null){
            return uri;
        }
        StringBuilder sb = new StringBuilder(uri);
        sb.append("?");
        for (Map.Entry map : params.entrySet()) {
            sb.append(map.getKey())
                    .append("=")
                    .append(map.getValue())
                    .append("&");
        }
        uri = sb.substring(0, sb.length() - 1);
        return uri;
    }

    public String get(String url, Map<String,String> map) {

        ResponseEntity responseEntity = restTemplate.getForEntity
                (
                        generateRequestParameters("http", url, map),
                        String.class
                );
        return (String) responseEntity.getBody();
    }

    public String post(String url, Map<String,String> jsonMap) {
        HttpEntity<Map<String, String>> mapHttpEntity = generatePostJson(jsonMap);
        ResponseEntity<String> apiResponse = restTemplate.postForEntity
                (
                        url,
                        mapHttpEntity,
                        String.class
                );
        return apiResponse.getBody();
    }
}