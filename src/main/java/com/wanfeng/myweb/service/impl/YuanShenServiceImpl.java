package com.wanfeng.myweb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wanfeng.myweb.Utils.HttpUtils.YuanShenHttpUtils;
import com.wanfeng.myweb.config.YuanshenConfig;
import com.wanfeng.myweb.service.YuanShenService;
import com.wanfeng.myweb.vo.PushVO;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YuanShenServiceImpl implements YuanShenService {
    private static final Logger log = LogManager.getLogger(YuanShenServiceImpl.class.getName());
    private static String msgToIphone = "";

    @Autowired
    private YuanShenHttpUtils yuanShenHttpUtils;
    @Autowired
    private PushServiceImpl pushIphoneService;

    @Override
    public void doTask() {
        List<Map<String, Object>> uid = getUid();
        for (Map<String, Object> uidMap : uid) {
            if (!(boolean) uidMap.get("flag")) {
                continue;
            }
            String doSign = doGetWard((String) uidMap.get("uid"), (String) uidMap.get("region"));
            String hubSign = hubSign((String) uidMap.get("uid"), (String) uidMap.get("region"));
            uidMap.put("msg", uidMap.get("msg") + "\n" + doSign + "\n" + hubSign);
        }
        try {
            pushIphoneService.pushIphone(new PushVO("原神", msgToIphone, "原神"));
        } catch (Exception e) {
            log.info("推送iPhone失败");
        } finally {
            msgToIphone = "";
        }
    }

    public String doGetWard(String uid, String region) {
        Map<String, Object> data = new HashMap<>();
        data.put("act_id", YuanshenConfig.ACT_ID);
        data.put("region", region);
        data.put("uid", uid);
        JSONObject signResult = YuanShenHttpUtils.doPost(YuanshenConfig.SIGN_URL, yuanShenHttpUtils.getHeaders(""), data);
        if (signResult.getInteger("retcode") == 0) {
            log.info("原神签到成功：{}", signResult.get("message"));
            msgToIphone += "原神签到成功：「" + signResult.get("message") + "」\n";
            return "原神签到福利成功：" + signResult.get("message");
        } else {
            log.info("原神签到失败：{}", signResult.get("message"));
            msgToIphone += "原神签到失败：「" + signResult.get("message") + "」\n";
            return "原神签到失败：" + signResult.get("message");
        }
    }

    public String hubSign(String uid, String region) {
        Map<String, Object> data = new HashMap<>();
        data.put("act_id", YuanshenConfig.ACT_ID);
        data.put("region", region);
        data.put("uid", uid);
        JSONObject signInfoResult = YuanShenHttpUtils.doGet(YuanshenConfig.INFO_URL, yuanShenHttpUtils.getHeaders(""), data);
        if (signInfoResult == null || signInfoResult.getJSONObject("data") == null) {
            return null;
        }
        LocalDateTime time = LocalDateTime.now();
        Boolean isSign = signInfoResult.getJSONObject("data").getBoolean("is_sign");
        Integer totalSignDay = signInfoResult.getJSONObject("data").getInteger("total_sign_day");
        int day = isSign ? totalSignDay : totalSignDay + 1;
        Award award = getAwardInfo(day);
        StringBuilder msg = new StringBuilder();
        msg.append(time.getMonth().getValue()).append("月已签到").append(totalSignDay).append("\n");
        msgToIphone += time.getMonth().getValue() + "月已签到: 「" + totalSignDay + "」天\n";
        msg.append(signInfoResult.getJSONObject("data").get("today")).append("签到获取").append(award.getCnt()).append(award.getName());
        log.info("{}月已签到{}天", time.getMonth().getValue(), totalSignDay);
        log.info("{}签到获取{}{}", signInfoResult.getJSONObject("data").get("today"), award.getCnt(), award.getName());
        msgToIphone += signInfoResult.getJSONObject("data").get("today") + "签到获取「" + award.getCnt() + award.getName() + "」";
        return msg.toString();
    }

    public Award getAwardInfo(int day) {
        JSONObject awardResult = YuanShenHttpUtils.doGet(YuanshenConfig.AWARD_URL, yuanShenHttpUtils.getHeaders(""));
        JSONArray jsonArray = awardResult.getJSONObject("data").getJSONArray("awards");
        List<Award> awards = JSON.parseObject(JSON.toJSONString(jsonArray), new TypeReference<List<Award>>() {
        });
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
                msgToIphone += "获取uid失败，cookie可能有误!\n";
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
                msgToIphone += "用户 「" + nickname + "」 登陆成功！\n";
                list.add(mapInfo);
            }
            return list;
        } catch (Exception e) {
            map.put("flag", false);
            map.put("msg", "获取uid失败，未知异常：" + e.getMessage());
            msgToIphone += "获取uid失败，未知异常：" + e.getMessage();
            list.add(map);
            return list;
        }
    }

    @Data
    public static class Award {
        private String icon;
        private String name;
        private Integer cnt;
    }


}