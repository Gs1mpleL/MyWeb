package com.wanfeng.myweb.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.Entity.SystemConfigEntity;
import com.wanfeng.myweb.Utils.HttpUtils.BiliHttpUtils;
import com.wanfeng.myweb.Utils.QrCodeUtils;
import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.config.BizException;
import com.wanfeng.myweb.kotlin.RSAUtils;
import com.wanfeng.myweb.service.BiliService;
import com.wanfeng.myweb.service.SystemConfigService;
import com.wanfeng.myweb.service.impl.biliTask.*;
import com.wanfeng.myweb.vo.PushVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class BiliServiceImpl implements BiliService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiliServiceImpl.class);
    @Resource
    private BarkPushService pushIphoneService;
    @Resource
    private DailyTask dailyTask;
    @Resource
    private MangaTask mangaTask;
    @Resource
    private BiliLiveTask biliLiveTask;
    @Resource
    private Silver2CoinTask silver2CoinTask;
    @Resource
    private CollectVipGift collectVipGift;
    @Resource
    private BiliCoinApply biliCoinApply;
    @Resource
    private ThrowCoinTask throwCoinTask;
    @Resource
    private BiliHttpUtils biliHttpUtils;
    @Resource
    private CompetitionGuessTask competitionGuessTask;

    @Resource
    private SystemConfigService systemConfigService;

    public static String getRefreshCsrf(String html) {
        try {
            Document document = Jsoup.parse(html);
            Elements elements = document.select("div[id='1-name']");
            return elements.text();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("cookie刷新失败");
        }
    }

    /**
     * 每日任务启动
     *
     * @param totalCookie 来自前端的cookie
     */
    @Override
    public void DailyTaskStart(String totalCookie) {
        if (totalCookie.equals("liuzhuohao123")) {
            biliTask(true);
        } else {
            ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(totalCookie));
            biliTask(false);
        }
    }

    /**
     * 刷新Cookie
     */
    @Override
    public void refreshCookie() {
        ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(systemConfigService.getById(1)));
        JSONObject isNeedRefresh = biliHttpUtils.getWithTotalCookie("https://passport.bilibili.com/x/passport-login/web/cookie/info");
        LOGGER.info("开始检查是否需要刷新Cookie:[{}]", isNeedRefresh);
        if (isNeedRefresh.getJSONObject("data").getString("refresh").equals("true")) {
            LOGGER.info("需要刷新，开始刷新！");
            doRefresh();
        }else {
            LOGGER.info("不需要刷新！");
        }
    }

    /**
     * 实现了使用二维码登陆,但需要手动扫二维码
     */
    @Override
    public String login() {
        try {
            String qrcode = getQrcode();
            LOGGER.info("获得了Refresh_Token [{}]", qrcode);
            return qrcode;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("登陆失败");
        }
    }

    /**
     * 执行B站日常任务
     * @param isInnerJob 是否是系统内部定时任务
     */
    public void biliTask(boolean isInnerJob) {
        if (isInnerJob) {
            ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(systemConfigService.getById(1)));
        }
        BiliUserData biliUserData = ThreadLocalUtils.get(BiliUserData.BILI_USER_DATA, BiliUserData.class);
        if (check()) {
            LOGGER.info("用户名: {}", biliUserData.getUname());
            biliUserData.info("用户名: {}", biliUserData.getUname());
            LOGGER.info("硬币: {}", biliUserData.getMoney());
            biliUserData.info("硬币: {}", biliUserData.getMoney());
            LOGGER.info("经验: {}", biliUserData.getCurrentExp());
            biliUserData.info("经验: {}", biliUserData.getCurrentExp());
            dailyTask.run();
            mangaTask.run();
            silver2CoinTask.run();
            collectVipGift.run();
            biliCoinApply.run();
            throwCoinTask.run();
            competitionGuessTask.run();
            biliLiveTask.run();
            biliSend();
        } else {
            biliUserData.setSendMsg("账户已失效，请在Secrets重新绑定你的信息");
            biliSend();
            throw new BizException("账户已失效，请在Secrets重新绑定你的信息");
        }
    }

    /**
     * 检查用户的状态
     */
    public boolean check() {
        BiliUserData biliUserData = ThreadLocalUtils.get(BiliUserData.BILI_USER_DATA, BiliUserData.class);
        JSONObject jsonObject = biliHttpUtils.getWithTotalCookie("https://api.bilibili.com/x/web-interface/nav");
        JSONObject object = jsonObject.getJSONObject("data");
        String code = jsonObject.getString("code");
        String SUCCESS = "0";
        if (SUCCESS.equals(code)) {
            /* 用户名 */
            biliUserData.setUname(object.getString("uname"));
            /* 账户的uid */
            biliUserData.setMid(object.getString("mid"));
            /* vip类型 */
            biliUserData.setVipType(object.getString("vipType"));
            /* 硬币数 */
            biliUserData.setMoney(object.getString("money"));
            /* 经验 */
            biliUserData.setCurrentExp(object.getJSONObject("level_info").getString("current_exp"));
            /* 大会员状态 */
            biliUserData.setVipStatus(object.getString("vipStatus"));
            /* 钱包B币卷余额 */
            biliUserData.setCouponBalance(object.getJSONObject("wallet").getString("coupon_balance"));
            return true;
        }
        return false;
    }

    public void biliSend() {
        BiliUserData biliUserData = ThreadLocalUtils.get(BiliUserData.BILI_USER_DATA, BiliUserData.class);
        String sendMsg = biliUserData.getSendMsg();
        String title = "哔哩哔哩";
        String groupName = "哔哩哔哩";
        pushIphoneService.pushIphone(new PushVO(title, sendMsg, groupName));
    }

    void doRefresh() {
        BiliUserData biliUserData = ThreadLocalUtils.get(BiliUserData.BILI_USER_DATA, BiliUserData.class);
        String refreshCsrf = getRefreshCsrf(biliHttpUtils.getHtml("https://www.bilibili.com/correspond/1/" + getCorrespondPath()));
        LOGGER.info("原的Cookie进行更新Cookie [{}]", biliUserData);
        String requestBody = "csrf=" + biliUserData.getBiliJct()
                + "&refresh_csrf=" + refreshCsrf
                + "&source=" + "main_web"
                + "&refresh_token=" + biliUserData.getRefreshToken();
        JSONObject post = biliHttpUtils.postForUpdateCookie("https://passport.bilibili.com/x/passport-login/web/cookie/refresh", requestBody);
        LOGGER.info("Cookie刷新结果[{}]", post);
        String newRefreshToken = post.getJSONObject("data").getString("refresh_token");
        SystemConfigEntity byId = systemConfigService.getById(1);
        byId.setBiliRefreshToken(newRefreshToken);
        systemConfigService.updateById(byId);
        LOGGER.info("刷新Cookie完成！ [{}]", byId);
    }

    String getCorrespondPath() {
        return new RSAUtils().encode();
    }

    public String getQrcode() {
        try {
            JSONObject jsonObject = biliHttpUtils.getWithTotalCookie("https://passport.bilibili.com/x/passport-login/web/qrcode/generate");
            String url = jsonObject.getJSONObject("data").getString("url");
            LOGGER.info("生成二维码 [{}]", url);
            String qrcode_key = jsonObject.getJSONObject("data").getString("qrcode_key");
            BufferedImage image = QrCodeUtils.createImage(url, "", false);
            File outputFile = new File("src/main/resources/static/images/your-image-name.png");

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
            e.printStackTrace();
            e.printStackTrace();
            throw new BizException("扫码登陆失败");
        }
    }
}
