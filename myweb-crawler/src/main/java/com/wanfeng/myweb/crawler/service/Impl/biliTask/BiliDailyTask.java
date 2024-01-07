package com.wanfeng.myweb.crawler.service.Impl.biliTask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.api.client.UserClient;
import com.wanfeng.myweb.common.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.crawler.config.BiliUserData;
import com.wanfeng.myweb.crawler.dto.Comment;
import com.wanfeng.myweb.crawler.service.BiliHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class BiliDailyTask implements Task {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(BiliDailyTask.class);
    @Resource
    private BiliHttpService biliHttpUtils;
    @Resource
    private UserClient userClient;

    @Override
    public void run() {
        try {
            BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
            JSONArray regions = getRegions("6", "1");
            JSONObject report = report(regions.getJSONObject(5).getString("aid"), regions.getJSONObject(5).getString("cid"), "300");
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
            LOGGER.error(e.getMessage());
            BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
            LOGGER.error(e.getMessage());
            LOGGER.error("每日任务异常 -- " + e);
            biliUserData.info("每日任务异常 -- " + e);
        }
    }

    public JSONObject setComment(String comment, String oid) {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        String body = "oid=" + oid
                + "&type=1"
                + "&message=" + comment
                + "&plat=1"
                + "&csrf=" + biliUserData.getBiliJct();
        return biliHttpUtils.postWithTotalCookie("https://api.bilibili.com/x/v2/reply/add", body);
    }
    public List<Comment> getCommentsByOid(String oid) {
        JSONObject jsonObject = biliHttpUtils.getWithTotalCookie("https://api.bilibili.com/x/v2/reply" + "?oid=" + oid + "&type=1");
        ArrayList<Comment> comments = new ArrayList<>();
        JSONArray replies = jsonObject.getJSONObject("data").getJSONArray("replies");
        for (int i = 0; i < replies.size(); i++) {
            JSONObject jsonObject1 = replies.getJSONObject(i);
            comments.add(new Comment(jsonObject1, oid));
        }
        return comments;
    }

    public boolean replyComment(Comment comment, String replyMsg) {
        BiliUserData biliUserData = ThreadLocalUtils.get(BiliUserData.BILI_USER_DATA, BiliUserData.class);
        String body = "csrf=" + biliUserData.getBiliJct() +
                "&oid=" + comment.getOid() +
                "&type=1" +
                "&message=" + replyMsg +
                "&plat=1" +
                "&root=" + comment.getReplyId() +
                "&parent=" + comment.getReplyId();
        JSONObject jsonObject = biliHttpUtils.postWithTotalCookie("https://api.bilibili.com/x/v2/reply/add", body);
        return jsonObject.getString("code").equals("0");
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
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
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
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        String body = "aid=" + aid + "&csrf=" + biliUserData.getBiliJct() + "&eab_x=2&ramval=0&source=web_normal&ga=1";
        return biliHttpUtils.postWithTotalCookie("https://api.bilibili.com/x/web-interface/share/add", body);
    }
}
