package com.wanfeng.myweb.bilibili.utils;

import com.alibaba.fastjson.JSONObject;

import com.wanfeng.common.Utils.ThreadLocalUtils;
import com.wanfeng.common.entity.SystemConfigEntity;
import com.wanfeng.common.service.SystemConfigService;
import com.wanfeng.myweb.bilibili.config.BiliUserData;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 封装的Bilibili网络请求请求工具类
 */
@Component
public class BiliHttpUtils {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(BiliHttpUtils.class);
    @Resource
    private SystemConfigService systemConfigService;

    private BiliHttpUtils() {
    }

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            // 信任所有
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            LOGGER.error(e.getMessage());
        }
        return HttpClients.createDefault();

    }

    public static String parseCookies(String cookieHeader, String match) {
        Pattern pattern = Pattern.compile(match + "=([^;]*)");
        Matcher matcher = pattern.matcher(cookieHeader);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    /**
     * 发送get请求
     *
     * @param url 请求的地址，包括参数
     */
    public JSONObject getWithTotalCookie(String url) {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        HttpClient client = createSSLClientDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("connection", "keep-alive");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        httpGet.addHeader("referer", "https://www.bilibili.com/");
        httpGet.addHeader("Cookie", biliUserData.getTotalCookie()); // 不知道缺哪个字段，索性全部使用
        HttpResponse resp;

        String respContent = null;
        try {
            resp = client.execute(httpGet);
            HttpEntity entity = resp.getEntity();
            respContent = EntityUtils.toString(entity, "UTF-8");
            LOGGER.info("请求[{}],结果[{}]",url,respContent);
            return JSONObject.parseObject(respContent);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("get请求错误 -- " + e);
            return JSONObject.parseObject(respContent);
        }
    }

    /**
     * 发送get请求
     *
     * @param url 请求的地址，包括参数
     */
    public JSONObject getForUpdateCookie(String url) {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        HttpClient client = createSSLClientDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("connection", "keep-alive");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        httpGet.addHeader("referer", "https://www.bilibili.com/");
        httpGet.addHeader("Cookie", biliUserData.getTotalCookie());
        HttpResponse resp;

        String respContent = null;
        try {
            resp = client.execute(httpGet);
            Header[] allHeaders = resp.getAllHeaders();
            StringBuilder needProcessCookie = new StringBuilder();
            for (Header allHeader : allHeaders) {
                if (allHeader.getName().equals("Set-Cookie")) {
                    needProcessCookie.append(Arrays.toString(allHeaders));
                }
            }
            String SESSDATA = parseCookies(needProcessCookie.toString(), "SESSDATA");
            String bili_jct = parseCookies(needProcessCookie.toString(), "bili_jct");
            String DedeUserID = parseCookies(needProcessCookie.toString(), "DedeUserID");
            String DedeUserID__ckMd5 = parseCookies(needProcessCookie.toString(), "DedeUserID__ckMd5");
            String sid = parseCookies(needProcessCookie.toString(), "sid");
            if (SESSDATA != null && bili_jct != null && DedeUserID != null && DedeUserID__ckMd5 != null && sid != null) {
                String newTotalCookie = "SESSDATA=" + SESSDATA + "; " + "bili_jct=" + bili_jct + "; " + "DedeUserID=" + DedeUserID + "; " + "DedeUserID__ckMd5=" + DedeUserID__ckMd5 + "sid=" + sid;
                LOGGER.info("登陆获得新Cookie [{}]", newTotalCookie);
                SystemConfigEntity byId = systemConfigService.getById(1);
                byId.setBiliCookie(newTotalCookie);
                systemConfigService.updateById(byId);
                LOGGER.info("更新数据库中Cookie");
                ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(byId));
                LOGGER.info("修改ThreadLocal中的Cookie:[{}]", ThreadLocalUtils.get(BiliUserData.BILI_USER_DATA, BiliUserData.class).getTotalCookie());
            }
            HttpEntity entity = resp.getEntity();
            respContent = EntityUtils.toString(entity, "UTF-8");
            return JSONObject.parseObject(respContent);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("Bili Get请求错误 -- " + e);
            return JSONObject.parseObject(respContent);
        }
    }

    public String getHtml(String url) {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        HttpClient client = createSSLClientDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("connection", "keep-alive");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        httpGet.addHeader("referer", "https://www.bilibili.com/");
        httpGet.addHeader("Cookie", biliUserData.getTotalCookie()); // 不知道缺哪个字段，索性全部使用
        HttpResponse resp;
        String respContent = null;
        try {
            resp = client.execute(httpGet);
            HttpEntity entity = resp.getEntity();
            respContent = EntityUtils.toString(entity, "UTF-8");
            return respContent;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("Bili Get请求错误 -- " + e);
            return respContent;
        }
    }

    /**
     * 发送post请求
     *
     * @param url  请求的地址
     * @param body 携带的参数
     */
    public JSONObject postWithTotalCookie(String url, String body) {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        StringEntity entityBody = new StringEntity(body, "UTF-8");
        HttpClient client = createSSLClientDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("connection", "keep-alive");
        httpPost.addHeader("referer", "https://www.bilibili.com/");
        httpPost.addHeader("accept", "application/json, text/plain, */*");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.addHeader("charset", "UTF-8");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        httpPost.addHeader("Cookie", biliUserData.getTotalCookie());
        httpPost.setEntity(entityBody);
        HttpResponse resp;
        String respContent = null;
        try {
            resp = client.execute(httpPost);
            HttpEntity entity;
            entity = resp.getEntity();
            respContent = EntityUtils.toString(entity, "UTF-8");
            LOGGER.info("Bili请求:[{}],结果[{}]", url, respContent);
            return JSONObject.parseObject(respContent);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("Bili Post请求错误 -- " + e);
            return JSONObject.parseObject(respContent);
        }
    }


    public JSONObject postForUpdateCookie(String url, String body) {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        StringEntity entityBody = new StringEntity(body, "UTF-8");
        HttpClient client = createSSLClientDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("connection", "keep-alive");
        httpPost.addHeader("referer", "https://www.bilibili.com/");
        httpPost.addHeader("accept", "application/json, text/plain, */*");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.addHeader("charset", "UTF-8");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        httpPost.addHeader("Cookie", biliUserData.getTotalCookie());
        httpPost.setEntity(entityBody);
        HttpResponse resp;
        String respContent = null;
        try {
            resp = client.execute(httpPost);
            Header[] allHeaders = resp.getAllHeaders();
            StringBuilder needProcessCookie = new StringBuilder();
            for (Header allHeader : allHeaders) {
                if (allHeader.getName().equals("Set-Cookie")) {
                    needProcessCookie.append(Arrays.toString(allHeaders));
                }
            }
            String SESSDATA = parseCookies(needProcessCookie.toString(), "SESSDATA");
            String bili_jct = parseCookies(needProcessCookie.toString(), "bili_jct");
            String DedeUserID = parseCookies(needProcessCookie.toString(), "DedeUserID");
            String DedeUserID__ckMd5 = parseCookies(needProcessCookie.toString(), "DedeUserID__ckMd5");
            String sid = parseCookies(needProcessCookie.toString(), "sid");
            if (SESSDATA != null && bili_jct != null && DedeUserID != null && DedeUserID__ckMd5 != null && sid != null) {
                String newTotalCookie = "SESSDATA=" + SESSDATA + "; " + "bili_jct=" + bili_jct + "; " + "DedeUserID=" + DedeUserID + "; " + "DedeUserID__ckMd5=" + DedeUserID__ckMd5 + "sid=" + sid;
                LOGGER.info("登陆获得新Cookie [{}]", newTotalCookie);
                SystemConfigEntity byId = systemConfigService.getById(1);
                byId.setBiliCookie(newTotalCookie);
                systemConfigService.updateById(byId);
                LOGGER.info("更新数据库中Cookie");
                ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(byId));
                LOGGER.info("修改ThreadLocal中的Cookie:[{}]", ThreadLocalUtils.get(BiliUserData.BILI_USER_DATA, BiliUserData.class).getTotalCookie());
            }
            HttpEntity entity;
            entity = resp.getEntity();
            respContent = EntityUtils.toString(entity, "UTF-8");
            return JSONObject.parseObject(respContent);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.info("Bili Post请求错误 -- " + e);
            return JSONObject.parseObject(respContent);
        }
    }


}
