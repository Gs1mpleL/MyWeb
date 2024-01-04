package com.wanfeng.myweb.bilibili.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.common.Utils.QrCodeUtils;
import com.wanfeng.common.Utils.ThreadLocalUtils;
import com.wanfeng.common.exception.BizException;
import com.wanfeng.myweb.api.client.UserClient;
import com.wanfeng.myweb.api.dto.SystemConfigDto;
import com.wanfeng.myweb.bilibili.config.BiliUserData;
import com.wanfeng.myweb.bilibili.service.BiliLoginService;
import com.wanfeng.myweb.bilibili.utils.BiliHttpUtils;
import com.wanfeng.myweb.bilibili.utils.RSAUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class BiliLoginServiceImpl implements BiliLoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiliLoginServiceImpl.class);
    @Autowired
    private UserClient userClient;
    @Autowired
    private BiliHttpUtils biliHttpUtils;
    @Override
    public void refreshCookie() throws Exception {
        SystemConfigDto systemConfig = userClient.getSystemConfig();
        LOGGER.info("远程获取的config[{}]",systemConfig);
        ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(systemConfig));
        JSONObject isNeedRefresh = biliHttpUtils.getWithTotalCookie("https://passport.bilibili.com/x/passport-login/web/cookie/info");
        LOGGER.info("开始检查是否需要刷新Cookie:[{}]", isNeedRefresh);
        if (isNeedRefresh.getJSONObject("data").getString("refresh").equals("true")) {
            LOGGER.info("需要刷新，开始刷新！");
            doRefresh();
        } else {
            LOGGER.info("不需要刷新！");
        }

    }

    @Override
    public String login() {
        try {
            String qrcode = getQrcode();
            LOGGER.info("获得了Refresh_Token [{}]", qrcode);
            SystemConfigDto systemConfig = userClient.getSystemConfig();
            systemConfig.setBiliRefreshToken(qrcode);
            userClient.updateSystemConfig(systemConfig);
            return qrcode;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new BizException("登陆失败");
        }
    }
    public String getQrcode() {
        try {
            JSONObject jsonObject = biliHttpUtils.getWithTotalCookie("https://passport.bilibili.com/x/passport-login/web/qrcode/generate");
            String url = jsonObject.getJSONObject("data").getString("url");
            LOGGER.info("生成二维码 [{}]", url);
            String qrcode_key = jsonObject.getJSONObject("data").getString("qrcode_key");
            BufferedImage image = QrCodeUtils.createImage(url, "", false);
            File outputFile = new File("a.png");

            LOGGER.info("二维码文件创建[{}]", ImageIO.write(image, "png", outputFile));
            for (int i = 0; i < 60; i++) {
                JSONObject jsonObject1 = biliHttpUtils.getForUpdateCookie("https://passport.bilibili.com/x/passport-login/web/qrcode/poll?qrcode_key=" + qrcode_key);
                if (jsonObject1.getJSONObject("data").getString("code").equals("0")) {
                    LOGGER.info("扫码登陆成功 [{}],返回refresh_token", jsonObject1);
                    return jsonObject1.getJSONObject("data").getString("refresh_token");
                } else {
                    LOGGER.info(String.valueOf(jsonObject1));
                    Thread.sleep(2000);
                }
            }
            LOGGER.info("二维码过期");
            throw new BizException("二维码过期");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new BizException("扫码登陆失败");
        }
    }

    void doRefresh() throws Exception {
        ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(userClient.getSystemConfig()));
        BiliUserData biliUserData = ThreadLocalUtils.get(BiliUserData.BILI_USER_DATA, BiliUserData.class);
        String refreshCsrf = getRefreshCsrf(biliHttpUtils.getHtml("https://www.bilibili.com/correspond/1/" + getCorrespondPath()));
        if(refreshCsrf.equals("") || refreshCsrf == null){
            throw new BizException("获取refreshCsrf失败");
        }
        LOGGER.info("原的Cookie进行更新Cookie [{}]", biliUserData);
        String requestBody = "csrf=" + biliUserData.getBiliJct()
                + "&refresh_csrf=" + refreshCsrf
                + "&source=" + "main_web"
                + "&refresh_token=" + biliUserData.getRefreshToken();
        JSONObject post = biliHttpUtils.postForUpdateCookie("https://passport.bilibili.com/x/passport-login/web/cookie/refresh", requestBody);
        LOGGER.info("Cookie刷新结果[{}]", post);
        String newRefreshToken = post.getJSONObject("data").getString("refresh_token");
        SystemConfigDto byId = userClient.getSystemConfig();
        byId.setBiliRefreshToken(newRefreshToken);
        userClient.updateSystemConfig(byId);
        LOGGER.info("刷新Cookie完成！ [{}]", byId);
    }
    public static String getRefreshCsrf(String html) {
        try {
            Document document = Jsoup.parse(html);
            Elements elements = document.select("div[id='1-name']");
            return elements.text();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new BizException("cookie刷新失败");
        }
    }
    String getCorrespondPath() throws Exception {
        return new RSAUtils().encode();
    }
}
