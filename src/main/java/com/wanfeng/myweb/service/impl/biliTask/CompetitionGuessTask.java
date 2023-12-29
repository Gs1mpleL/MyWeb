package com.wanfeng.myweb.service.impl.biliTask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanfeng.myweb.Utils.HttpUtils.BiliHttpUtils;
import com.wanfeng.myweb.Utils.ThreadLocalUtils;
import com.wanfeng.myweb.config.BiliUserData;
import com.wanfeng.myweb.po.GuessGame;
import com.wanfeng.myweb.po.GuessTeam;
import com.wanfeng.myweb.properties.BiliProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class CompetitionGuessTask implements Task {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompetitionGuessTask.class);
    @Resource
    private BiliHttpUtils biliHttpUtils;
    @Resource
    private BiliProperties biliProperties;

    @Override
    public void run() {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        try {
            ArrayList<GuessGame> guessingList = getGuessingList();
            if (guessingList == null || guessingList.size() == 0) {
                return;
            }
            for (GuessGame guessGame : guessingList) {
                // 只在比赛前一天进行竞猜
                if (isOneDayBeforeOrNow(guessGame.getEndTime())) {
                    doGuess(guessGame);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("竞猜失败:{}", e.getMessage());
            biliUserData.info("竞猜失败:{}", e.getMessage());
        }
    }


    private void doGuess(GuessGame guessGame) {
        BiliUserData biliUserData = ThreadLocalUtils.get(ThreadLocalUtils.BILI_USER_DATA, BiliUserData.class);
        ArrayList<GuessTeam> guessOptions = guessGame.getGuessOptions();
        double odds = Double.parseDouble(guessOptions.get(0).getTeamRate());
        GuessTeam chooseOpt = guessOptions.get(0);
        for (GuessTeam guessOption : guessOptions) {
            // 反向压
            if (odds < Double.parseDouble(guessOption.getTeamRate())) {
                odds = Double.parseDouble(guessOption.getTeamRate());
                chooseOpt = guessOption;
            }
        }
        // 投币给胜率高的队伍
        String body = "oid=" + guessGame.getContestId()
                + "&main_id=" + guessGame.getMainId()
                + "&detail_id=" + chooseOpt.getTeamId()
                + "&count=" + biliProperties.getGuessCoin()
                + "&is_fav=1"
                + "&csrf=" + biliUserData.getBiliJct();
        JSONObject post = biliHttpUtils.postWithTotalCookie("https://api.bilibili.com/x/esports/guess/add", body);
        if (post.getString("code").equals("0")) {
            LOGGER.info("在{}中投给{}{}个硬币", guessGame.getTitle(), chooseOpt.getTeamName(), biliProperties.getGuessCoin());
            biliUserData.info("在" + guessGame.getTitle() + "中投给" + chooseOpt.getTeamName() + biliProperties.getGuessCoin() + "个硬币");
        } else {
            LOGGER.info("在{" + guessGame.getTitle() + "}中竞猜: {}", post.getString("message"));
            biliUserData.info("在「" + guessGame.getTitle() + "」竞猜: {}", post.getString("message"));
        }
    }

    private ArrayList<GuessGame> getGuessingList() {
        ArrayList<GuessGame> guessGameArrayList = new ArrayList<>();
        JSONObject jsonObject = biliHttpUtils.getWithTotalCookie("https://api.bilibili.com/x/esports/guess/collection/question?pn=1&ps=50");
        if (jsonObject.getString("code").equals("0")) {
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
            for (Object o : jsonArray) {
                JSONObject json = (JSONObject) o;
                guessGameArrayList.add(new GuessGame(json));
            }
            return guessGameArrayList;
        } else {
            return null;
        }
    }

    boolean isOneDayBeforeOrNow(String timestamp) {
        Instant instant = Instant.ofEpochSecond(Long.parseLong(timestamp));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime dateTime = instant.atZone(ZoneId.of("UTC")).toLocalDateTime();
        String formattedDateTime = dateTime.format(formatter);
        LocalDate date = LocalDate.parse(formattedDateTime);
        LocalDate today = LocalDate.now();
        LocalDate yesterdayOfEnd = date.minus(Period.ofDays(1));
        // 判断今天是否是输入日期的前一天或今天
        return today.equals(yesterdayOfEnd) || today.equals(date);
    }
}
