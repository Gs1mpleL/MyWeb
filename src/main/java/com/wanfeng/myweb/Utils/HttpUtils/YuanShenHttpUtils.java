package com.wanfeng.myweb.Utils.HttpUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.config.YuanshenConfig;
import com.wanfeng.myweb.properties.YuanShenProperties;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
@Data
@Component
public class YuanShenHttpUtils {

    private String clientType = "";

    private String appVersion = "";

    private String salt = "";

    private String type = "5";
    @Autowired
    private YuanShenProperties yuanShenProperties;
    public Header[] getBasicHeaders() {
        setClientType(YuanshenConfig.SIGN_CLIENT_TYPE);
        setAppVersion(YuanshenConfig.APP_VERSION);
        setSalt(YuanshenConfig.SIGN_SALT);
        return new YuanShenHttpUtils.HeaderBuilder.Builder()
                .add("Cookie", yuanShenProperties.getCookie())
                .add("User-Agent", String.format(YuanshenConfig.USER_AGENT_TEMPLATE, getAppVersion()))
                .add("Referer", YuanshenConfig.refererURL)
                .add("Accept-Encoding", "gzip, deflate, br")
                .add("x-rpc-channel", "appstore")
                .add("accept-language", "zh-CN,zh;q=0.9,ja-JP;q=0.8,ja;q=0.7,en-US;q=0.6,en;q=0.5")
                .add("accept-encoding", "gzip, deflate")
                .add("accept-encoding", "gzip, deflate")
                .add("x-requested-with", "com.mihoyo.hyperion")
                .add("Host", "api-takumi.mihoyo.com").build();
    }
    public Header[] getHeaders(String dsType) {
        return new YuanShenHttpUtils.HeaderBuilder.Builder()
                .add("x-rpc-device_id", UUID.randomUUID().toString().replace("-", "").toUpperCase())
                .add("Content-Type", "application/json;charset=UTF-8")
                .add("x-rpc-client_type", getClientType())
                .add("x-rpc-app_version", getAppVersion())
                .add("DS", getDS()).addAll(getBasicHeaders()).build();
    }
    private static final Logger logger = LogManager.getLogger(YuanShenHttpUtils.class.getName());
    public static JSONObject doGet(String url, Header[] headers, Map<String, Object> data) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject resultJson = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            List<NameValuePair> params;
            if (data != null && !data.isEmpty()) {
                params = new ArrayList<>();
                for (String key : data.keySet()) {
                    params.add(new BasicNameValuePair(key, data.get(key) + ""));
                }
                uriBuilder.setParameters(params);
            }
            URI uri = uriBuilder.build();
            HttpGet httpGet = new HttpGet(uri);
            if (headers != null && headers.length != 0) {
                for (Header header : headers) {
                    httpGet.addHeader(header);
                }
            }
            httpGet.setConfig(REQUEST_CONFIG);
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                resultJson = JSON.parseObject(result);
            } else {
                logger.warn(response.getStatusLine().getStatusCode() + "配置已失效，请更新配置信息");
            }
            return resultJson;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            closeResource(httpClient, response);
        }
        return resultJson;
    }
    public static JSONObject doPost(String url, Header[] headers, Map<String, Object> data) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject resultJson = null;
        try {
            StringEntity entity = new StringEntity(JSON.toJSONString(data), StandardCharsets.UTF_8);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(entity);
            if (headers != null && headers.length != 0) {
                for (Header header : headers) {
                    httpPost.addHeader(header);
                }
            }
            httpPost.setConfig(REQUEST_CONFIG);
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                resultJson = JSON.parseObject(result);
            } else {
                logger.warn(response.getStatusLine().getStatusCode() + "配置已失效，请更新配置信息");
            }
            return resultJson;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeResource(httpClient, response);
        }
        return resultJson;
    }

    public static JSONObject doGet(String url, Header[] headers) {
        return doGet(url, headers, null);
    }
    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom().setConnectTimeout(35000)
            .setConnectionRequestTimeout(35000)
            .setSocketTimeout(60000)
            .setRedirectsEnabled(true)
            .build();

    private static void closeResource(CloseableHttpClient httpClient, CloseableHttpResponse response) {
        if (null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static class HeaderBuilder {

        public static class Builder {
            private final Map<String, String> header = new HashMap<>();
            public Builder add(String name, String value) {
                this.header.put(name, value);
                return this;
            }
            public Builder addAll(Header[] headers) {
                for (Header h : headers) {
                    this.header.put(h.getName(), h.getValue());
                }
                return this;
            }
            public Header[] build() {
                List<Header> list = new ArrayList<>();
                for (String key : this.header.keySet()) {
                    list.add(new BasicHeader(key, this.header.get(key)));
                }
                return list.toArray(new Header[0]);
            }
        }
    }
    protected String getDS() {
        String i = (System.currentTimeMillis() / 1000) + "";
        String r = getRandomStr();
        return createDS(getSalt(), i, r);
    }
    protected String getDS(String gidsJson) {
        Random random = new Random();
        String i = (System.currentTimeMillis() / 1000) + "";
        String r = String.valueOf(random.nextInt(200000 - 100000) + 100000 + 1);
        return createDS(YuanshenConfig.COMMUNITY_SIGN_SALT, i, r, gidsJson);
    }
    private String createDS(String n, String i, String r) {
        String c = DigestUtils.md5Hex("salt=" + n + "&t=" + i + "&r=" + r);
        return String.format("%s,%s,%s", i, r, c);
    }
    private String createDS(String n, String i, String r, String b) {
        String c = DigestUtils.md5Hex("salt=" + n + "&t=" + i + "&r=" + r + "&b=" + b + "&q=" + "");
        return String.format("%s,%s,%s", i, r, c);
    }
    protected String getRandomStr() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 6; i++) {
            String CONSTANTS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            int number = random.nextInt(CONSTANTS.length());
            char charAt = CONSTANTS.charAt(number);
            sb.append(charAt);
        }
        return sb.toString();
    }
}
