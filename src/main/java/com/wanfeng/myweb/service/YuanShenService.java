package com.wanfeng.myweb.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wanfeng.myweb.Utils.YuanShenHttpUtils;
import com.wanfeng.myweb.config.YuanshenConfig;
import com.wanfeng.myweb.properties.YuanShenProperties;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class YuanShenService {
    private static final Logger log = LogManager.getLogger(YuanShenService.class.getName());
    @Autowired
    private YuanShenProperties yuanShenProperties;
    @Autowired
    private YuanShenHttpUtils yuanShenHttpUtils;

    public List<Map<String, Object>> doSign() {
        List<Map<String, Object>> uid = getUid();

        for (Map<String, Object> uidMap : uid) {
            if (!(boolean) uidMap.get("flag")) {
                continue;
            }

            String doSign = doSign((String) uidMap.get("uid"), (String) uidMap.get("region"));

            String hubSign = hubSign((String) uidMap.get("uid"), (String) uidMap.get("region"));
            uidMap.put("msg", uidMap.get("msg") + "\n" + doSign + "\n" + hubSign);
            continue;
        }
        return uid;
    }

    public String doSign(String uid, String region) {

        Map<String, Object> data = new HashMap<>();

        data.put("act_id", YuanshenConfig.ACT_ID);
        data.put("region", region);
        data.put("uid", uid);

        JSONObject signResult = YuanShenHttpUtils.doPost(YuanshenConfig.SIGN_URL+"?region="+region+"&act_id=e202009291139501&uid="+uid, yuanShenHttpUtils.getHeaders(""), data);

        if (signResult.getInteger("retcode") == 0) {
            log.info("原神签到福利成功：{}", signResult.get("message"));
            return "原神签到福利成功：" + signResult.get("message");
        } else {
            log.info("原神签到福利签到失败：{}", signResult.get("message"));
            return "原神签到福利签到失败：" + signResult.get("message");
        }
    }
    public String hubSign(String uid, String region) {
        Map<String, Object> data = new HashMap<>();

        data.put("act_id", YuanshenConfig.ACT_ID);
        data.put("region", region);
        data.put("uid", uid);

        JSONObject signInfoResult = YuanShenHttpUtils.doGet(YuanshenConfig.INFO_URL, yuanShenHttpUtils.getHeaders(""), data);
        if (signInfoResult == null || signInfoResult.getJSONObject("data") == null){
            return null;
        }

        LocalDateTime time = LocalDateTime.now();
        Boolean isSign = signInfoResult.getJSONObject("data").getBoolean("is_sign");
        Integer totalSignDay = signInfoResult.getJSONObject("data").getInteger("total_sign_day");
        int day = isSign ? totalSignDay : totalSignDay + 1;

        Award award = getAwardInfo(day);

        StringBuilder msg = new StringBuilder();
        msg.append(time.getMonth().getValue()).append("月已签到").append(totalSignDay).append("\n");
        msg.append(signInfoResult.getJSONObject("data").get("today")).append("签到获取").append(award.getCnt()).append(award.getName());

        log.info("{}月已签到{}天", time.getMonth().getValue(), totalSignDay);
        log.info("{}签到获取{}{}", signInfoResult.getJSONObject("data").get("today"), award.getCnt(), award.getName());

        return msg.toString();
    }
    @Data
    public class Award {

        private String icon;

        private String name;

        private Integer cnt;
    }
    public Award getAwardInfo(int day) {
        Map<String, String> data = new HashMap<>();

        data.put("act_id", YuanshenConfig.ACT_ID);
        data.put("region", YuanshenConfig.REGION);

        JSONObject awardResult = YuanShenHttpUtils.doGet(YuanshenConfig.AWARD_URL, yuanShenHttpUtils.getHeaders(""));
        JSONArray jsonArray = awardResult.getJSONObject("data").getJSONArray("awards");

        List<Award> awards = JSON.parseObject(JSON.toJSONString(jsonArray), new TypeReference<List<Award>>() {});
        return awards.get(day - 1);
    }
    public List<Map<String, Object>> getUid() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        try {
            JSONObject result = YuanShenHttpUtils.doGet(YuanshenConfig.ROLE_URL, yuanShenHttpUtils.getBasicHeaders());
            if (result == null) {
                map.put("flag", false);
                map.put("msg", "获取uid失败，cookie可能有误！");
                list.add(map);
                return list;
            }
            JSONArray jsonArray = result.getJSONObject("data").getJSONArray("list");
            for (Object user : jsonArray) {
                JSONObject userInfo = (JSONObject) user;
                String uid = userInfo.getString("game_uid");
                String nickname = userInfo.getString("nickname");
                String regionName = userInfo.getString("region_name");
                String region = userInfo.getString("region");
                log.info("获取用户UID：{}", uid);
                log.info("当前用户名称：{}", nickname);
                log.info("当前用户服务器：{}", regionName);
                Map<String, Object> mapInfo = new HashMap<>();
                mapInfo.put("uid", uid);
                mapInfo.put("nickname", nickname);
                mapInfo.put("region", region);
                mapInfo.put("flag", true);
                mapInfo.put("msg", "登录成功！用户UID：" + uid + "，用户名：" + nickname);
                list.add(mapInfo);
            }

            return list;
        } catch (Exception e) {
            map.put("flag", false);
            map.put("msg", "获取uid失败，未知异常：" + e.getMessage());
            list.add(map);
            return list;
        }
    }


}