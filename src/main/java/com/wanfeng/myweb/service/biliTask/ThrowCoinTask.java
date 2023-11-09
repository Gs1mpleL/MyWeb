package com.wanfeng.myweb.service.biliTask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.Utils.BiliRequest;
import com.wanfeng.myweb.config.BiliData;
import com.wanfeng.myweb.properties.BiliProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ThrowCoinTask implements Task {
    /** 获取日志记录器对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThrowCoinTask.class);
    @Autowired
    private BiliData biliData;
    @Autowired
    private DailyTask dailyTask;
    @Autowired
    private BiliRequest biliRequest;

    @Autowired
    private BiliProperties biliProperties;

    @Override
    public void run() {
        try {
            /* 今天投币获得了多少经验  此版本以及失效*/
            Integer reward = getReward();
            /* 计算今天需要投 num1 个硬币 */
            Integer num1 = (50 - reward) / 10;
            /* 还剩多少个硬币 */
            Integer num2 = getCoin();
            /* 配置类中设置投币数 */
            Integer num3 = biliProperties.getCoin();
            /* 避免设置投币数为负数异常 */
            num3 = num3 < 0 ? 0 : num3;
            /* 实际需要投 num个硬币 */
            int num = (num2 >= num1 ? num1 : num2) >= num3 ? num3 : (num2 >= num1 ? num1 : num2);
            if (num == 0){
                LOGGER.info("今日已投币 -- 5");
                biliData.info("今日已投币 -- {}", String.valueOf(5));
                return;
            }
            /* 获取分区视频信息 */
            JSONArray regions = getRegions("6", "1");
            /* 给每个视频投 1 个币,点 1 个赞 */
            for (int i = 0; i < num; i++) {
                /* 视频的aid */
                System.out.println(regions.getJSONObject(i));
                String aid = regions.getJSONObject(i).getString("aid");
                JSONObject json = throwCoin(aid, "1", "1");
                dailyTask.setComment("我来投币了，我也不知道我在干什么，因为我只是一个机器人", aid);
                /* 输出的日志消息 */
                String msg ;
                if ("0".equals(json.getString("code"))) {
                    msg = "硬币-1";
                } else {
                    msg = json.getString("message");
                }
                LOGGER.info("投币给 -- av{} -- {}", aid, msg);
                biliData.info("投币给 -- av{}", aid+"-"+ msg);
            }
        } catch (Exception e) {
            LOGGER.info("投币异常 -- " + e);
            biliData.info("投币异常 -- " + e);
        }
    }

    /**
     * 给视频投币
     * @param aid         视频 aid 号
     * @param num         投币数量
     * @param selectLike 是否点赞
     * @return JSONObject
     */
    public JSONObject throwCoin(String aid, String num, String selectLike) {

        String body = "aid=" + aid
                + "&multiply=" + num
                + "&select_like=" + selectLike
                + "&cross_domain=" + "true"
                + "&csrf=" + biliProperties.getBiliJct();
        return biliRequest.post("https://api.bilibili.com/x/web-interface/coin/add", body);
    }

    /**
     * 获取今天投币所得经验
     * @return JSONObject
     */
    public Integer getReward() {
        JSONObject jsonObject = biliRequest.get("https://api.bilibili.com/x/member/web/exp/log");
        int count = 0;
        LocalDate today = LocalDate.now();
        String regex = "\\b" + today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "\\b";
        Pattern pattern = Pattern.compile(regex);

        for (Object obj : jsonObject.getJSONObject("data").getJSONArray("list")) {
            String data = obj.toString();
            Matcher matcher = pattern.matcher(data);
            if (matcher.find()) {
                if (data.contains("视频投币奖励")){
                    count++;
                }
            }else {
                break;
            }
        }


        return count *10;
    }

    /**
     * 获取硬币的剩余数
     * @return Integer
     * 
     */
    public Integer getCoin() {
        JSONObject jsonObject = biliRequest.get("https://api.bilibili.com/x/web-interface/nav?build=0&mobi_app=web");
        return (int) (Double.parseDouble(jsonObject.getJSONObject("data").getString("money")));
    }

    /**
     * 获取B站分区视频信息
     * @param ps  获取视频的数量
     * @param rid 分区号
     * @return JSONArray
     * 
     */
    public JSONArray getRegions(String ps, String rid) {
        String params = "?ps=" + ps + "&rid=" + rid;
        JSONObject jsonObject = biliRequest.get("https://api.bilibili.com/x/web-interface/dynamic/region" + params);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("archives");
        JSONArray jsonRegions = new JSONArray();
        for (Object object : jsonArray) {
            JSONObject json = (JSONObject) object;
            JSONObject cache = new JSONObject();
            cache.put("title", json.getString("title"));
            cache.put("aid", json.getString("aid"));
            cache.put("bvid", json.getString("bvid"));
            cache.put("cid", json.getString("cid"));
            jsonRegions.add(cache);
        }
        return jsonRegions;
    }
}
