package com.wanfeng.myweb.service.impl.biliTask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.Utils.HttpUtils.BiliHttpUtils;
import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.config.BizException;
import com.wanfeng.myweb.service.PushService;
import com.wanfeng.myweb.service.SystemConfigService;
import com.wanfeng.myweb.vo.PushVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class DailyTask implements Task {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyTask.class);
    @Autowired
    private BiliHttpUtils biliHttpUtils;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private PushService pushService;

    @Override
    public void run() {
        try {
            BiliUserData biliUserData = ThreadLocalUtils.get("biliUserData", BiliUserData.class);
            JSONArray regions = getRegions("6", "1");
            JSONObject report = report(regions.getJSONObject(5).getString("aid"),
                    regions.getJSONObject(5).getString("cid"), "300");
            LOGGER.info("模拟观看视频 -- {}", "0".equals(report.getString("code")) ? "成功" : "失败");
            biliUserData.info("模拟观看视频 -- {}", "0".equals(report.getString("code")) ? "成功" : "失败");
            Thread.sleep(1000); // 这样好像分享视频成功率高点
            JSONObject share = share(regions.getJSONObject(5).getString("aid"));
            LOGGER.info("模拟分享视频 -- {}", "0".equals(share.getString("code")) ? "成功" : "失败");
            biliUserData.info("模拟分享视频 -- {}", "0".equals(share.getString("code")) ? "成功" : "失败");
            String aid = regions.getJSONObject(0).getString("aid");
            JSONObject commentRet = setComment("我点开了你的视频，我也不知道我在干什么，因为我只是一个机器人", aid);
            LOGGER.info("视频评论 [{}:{}]->{}", aid, "0".equals(commentRet.getString("code")) ? "成功" : "失败", commentRet.getString("message"));
            biliUserData.info("视频评论 -- {}", "0".equals(commentRet.getString("code")) ? "成功" : "失败");


        } catch (Exception e) {
            BiliUserData biliUserData = ThreadLocalUtils.get("biliUserData", BiliUserData.class);
            e.printStackTrace();
            LOGGER.error("每日任务异常 -- " + e);
            biliUserData.info("每日任务异常 -- " + e);
        }
    }

    public JSONObject setComment(String comment, String oid) {
        BiliUserData biliUserData = ThreadLocalUtils.get("biliUserData", BiliUserData.class);
        String body = "oid=" + oid
                + "&type=1"
                + "&message=" + comment
                + "&plat=1"
                + "&csrf=" + biliUserData.getBiliJct();
        return biliHttpUtils.postWithTotalCookie("https://api.bilibili.com/x/v2/reply/add", body);
    }

    /**
     * 获取B站推荐视频
     *
     * @param ps  代表你要获得几个视频
     * @param rid B站分区推荐视频
     */
    public JSONArray getRegions(String ps, String rid) {
        String params = "?ps=" + ps + "&rid=" + rid;
        JSONObject jsonObject = biliHttpUtils.getWithTotalCookie("https://api.bilibili.com/x/web-interface/dynamic/region" + params);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("archives");
        JSONArray jsonRegions = new JSONArray();
        for (Object object : jsonArray) {
            JSONObject json = (JSONObject) object;
            JSONObject cache = new JSONObject();
            cache.put("title", json.getString("title"));
            cache.put("aid", json.getString("aid"));
            cache.put("bvid", json.getString("bvid"));
            cache.put("cid", json.getString("cid"));
            cache.put("desc", json.getString("desc"));
            jsonRegions.add(cache);
        }
        return jsonRegions;
    }

    /**
     * 模拟观看视频
     *
     * @param aid 视频 aid 号
     * @param cid 视频 cid 号
     */
    public JSONObject report(String aid, String cid, String progres) {
        BiliUserData biliUserData = ThreadLocalUtils.get("biliUserData", BiliUserData.class);
        String body = "aid=" + aid
                + "&cid=" + cid
                + "&progres=" + progres
                + "&csrf=" + biliUserData.getBiliJct();
        return biliHttpUtils.postWithTotalCookie("http://api.bilibili.com/x/v2/history/report", body);
    }

    /**
     * 分享指定的视频
     *
     * @param aid 视频的aid
     */
    public JSONObject share(String aid) {
        BiliUserData biliUserData = ThreadLocalUtils.get("biliUserData", BiliUserData.class);
        String body = "aid=" + aid + "&csrf=" + biliUserData.getBiliJct() + "&eab_x=2&ramval=0&source=web_normal&ga=1";
        return biliHttpUtils.postWithTotalCookie("https://api.bilibili.com/x/web-interface/share/add", body);
    }


    /**
     * 每2分钟秒评论一条视频
     */
    public void commentTask() throws InterruptedException {
        ThreadLocalUtils.put(BiliUserData.BILI_USER_DATA, new BiliUserData(systemConfigService.getById(1)));
        pushService.pushIphone(new PushVO("哔哩哔哩","B站评论任务启动！","哔哩哔哩"));
        int[] typeList = new int[]{1, 3, 4, 5, 11, 13, 17, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 36, 37, 47, 51, 59, 65, 71, 75, 76, 83, 85, 86, 95};
        int count = 0;
        try {
            for (int typeId : typeList) {
                JSONArray regions = getRegions("10", String.valueOf(typeId));
                for (int i = 0; i < regions.size(); i++) {
                    count++;
                    JSONObject video = regions.getJSONObject(i);
                    System.out.println(video);
                    String aid = video.getString("aid");
                    String desc = video.getString("desc");
                    String title = video.getString("title");
                    StringBuilder titleSb = new StringBuilder(title);
                    String titleRev = titleSb.reverse().toString();
                    StringBuilder descSb = new StringBuilder(desc);
                    String descRev = descSb.reverse().toString();
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formatDateTime = now.format(formatter);
                    formatDateTime = new StringBuilder(formatDateTime).reverse().toString();
                    String total = "很好的视频，使我的字符串反转：\n[题标]->" + titleRev + "\n[介简]->" + descRev + "\n[间时]->" + formatDateTime;
                    JSONObject jsonObject = setComment(total, aid);
                    LOGGER.info("视频评论 [{}:{}]->{}", aid, "0".equals(jsonObject.getString("code")) ? "成功" : "失败", jsonObject.getString("message"));
                    Thread.sleep(120000);
                }
            }
            LOGGER.info("评论任务完成，共执行[{}]次",count);
            pushService.pushIphone(new PushVO("哔哩哔哩","评论任务完成，共执行[\"+count+\"]次","哔哩哔哩"));
        }catch (Exception e){
            LOGGER.info("评论任务失败，共执行[{}]次",count);
            pushService.pushIphone(new PushVO("哔哩哔哩","评论任务失败，共执行[\"+count+\"]次","哔哩哔哩"));
            throw new BizException("评论任务失败");
        }

    }
}
