package com.wanfeng.myweb.Utils;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.properties.BiliProperties;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 封装的网络请求请求工具类
 */
@Component
public class BiliRequest {
    @Autowired
    private BiliProperties biliProperties;
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(BiliRequest.class);

    private BiliRequest(){}

    /**
     * 发送get请求
     * @param url 请求的地址，包括参数
     */
    public static JSONObject get(String url){
        HttpClient client = createSSLClientDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("connection","keep-alive");
        httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        httpGet.addHeader("referer","https://www.bilibili.com/");
        httpGet.addHeader("Cookie","i-wanna-go-back=-1; buvid_fp_plain=undefined; DedeUserID=287473737; DedeUserID__ckMd5=6119bb91868bfa11; CURRENT_BLACKGAP=0; LIVE_BUVID=AUTO2616590964380964; rpdid=|(mmRk)u~uu0J'uY~|kl|Rmm; CURRENT_PID=c4b2e3e0-ca3c-11ed-b97c-0d30cc0d4e0c; FEED_LIVE_VERSION=V8; hit-new-style-dyn=1; buvid3=5560FB6F-AF10-0493-E00D-516378EA3E1566169infoc; b_nut=1690636066; _uuid=CABC91E2-AAA1-81084-3428-B810244EFA6DC37232infoc; buvid4=D3276578-4407-9E1D-1A12-D1470EB5B04B99252-022072920-aN5fltImCgRRESvvOMIdJsD%2FtyocAbctMgG2OwTErsdOJFoR0lo7jg%3D%3D; b_ut=5; is-2022-channel=1; CURRENT_FNVAL=4048; iflogin_when_web_push=1; hit-dyn-v2=1; CURRENT_QUALITY=64; enable_web_push=ENABLE; header_theme_version=CLOSE; SESSDATA=1cbd89c2%2C1714574541%2C48cff%2Ab1CjD6pUKVZwqbLXLNWWfBGG3m9qEpDkdWyA2ibXJooWrWxBBTIAw7WLLzw2UT1u-d0JcSVjhnSGlBd0Jqd0VqQV9DTnJZa2t0S2VFQ0E4TVVTSG5uUXk4NVFVdFZKQ2hyZzFSdVhJVmhFc0d6TDdFOXgtNEpYNG5WWmJabnBpVWFUMGRPV1J3YXlnIIEC; bili_jct=677dbf072d720a3a129d5fe3d96491f2; PVID=1; home_feed_column=5; browser_resolution=1686-821; bili_ticket=eyJhbGciOiJIUzI1NiIsImtpZCI6InMwMyIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTk0MzQ3MDgsImlhdCI6MTY5OTE3NTQ0OCwicGx0IjotMX0.hEGYtVBZ56I8eRszcKXcffX1lRpb-6jcRZVm1gHj_N8; bili_ticket_expires=1699434648; fingerprint=40e20e9e64dbaa6db6d63b0bc9caea69; b_lsid=F383B234_18BA3F8361E; buvid_fp=40e20e9e64dbaa6db6d63b0bc9caea69; sid=8myev34o; innersign=0; bp_video_offset_287473737=860809470181113877");
        HttpResponse resp ;
        String respContent = null;
        try{
            resp = client.execute(httpGet);
            HttpEntity entity = resp.getEntity();
            respContent = EntityUtils.toString(entity, "UTF-8");
            return JSONObject.parseObject(respContent);
        } catch (Exception e){
            e.printStackTrace();
            LOGGER.info("get请求错误 -- "+e);
            return JSONObject.parseObject(respContent);
        }
    }

    /**
     * 发送post请求
     * @param url 请求的地址
     * @param body 携带的参数
     */
    public JSONObject post(String url , String body){
        StringEntity entityBody = new StringEntity(body,"UTF-8");
        HttpClient client = createSSLClientDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("connection","keep-alive");
        httpPost.addHeader("referer","https://www.bilibili.com/");
        httpPost.addHeader("accept","application/json, text/plain, */*");
        httpPost.addHeader("Content-Type","application/x-www-form-urlencoded");
        httpPost.addHeader("charset","UTF-8");
        httpPost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        httpPost.addHeader("Cookie", biliProperties.getCookie());
        httpPost.setEntity(entityBody);
        HttpResponse resp ;
        String respContent = null;
        try{
            resp = client.execute(httpPost);
            HttpEntity entity;
            entity = resp.getEntity();
            respContent = EntityUtils.toString(entity, "UTF-8");
            return JSONObject.parseObject(respContent);
        } catch (Exception e){
            LOGGER.info("post请求错误 -- "+e);
            return JSONObject.parseObject(respContent);
        }
    }

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();

    }

}
