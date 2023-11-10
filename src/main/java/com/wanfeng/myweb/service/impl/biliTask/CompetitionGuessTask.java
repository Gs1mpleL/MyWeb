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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class CompetitionGuessTask implements Task{
    private static final Logger LOGGER = LoggerFactory.getLogger(CompetitionGuessTask.class);
    @Autowired
    private BiliHttpUtils biliHttpUtils;
    @Autowired
    private BiliProperties biliProperties;
    @Override
    public void run() {
        BiliUserData biliUserData = ThreadLocalUtils.get("biliUserData", BiliUserData.class);
        try {
            ArrayList<GuessGame> guessingList = getGuessingList();
            if (guessingList == null || guessingList.size() == 0){
                return;
            }
            for (GuessGame guessGame : guessingList) {
                doGuess(guessGame);
            }
        }catch (Exception e){
            LOGGER.info("竞猜失败:{}",e.getMessage());
            biliUserData.info("竞猜失败:{}",e.getMessage());
        }
    }


    private void doGuess(GuessGame guessGame) {
        BiliUserData biliUserData = ThreadLocalUtils.get("biliUserData", BiliUserData.class);
        // 投币给胜率高的队伍
        GuessTeam voteTeam = Double.parseDouble(guessGame.getGuessTeam1().getTeamRate())<Double.parseDouble(guessGame.getGuessTeam2().getTeamRate())?guessGame.getGuessTeam1():guessGame.getGuessTeam2();
        String body = "oid=" + guessGame.getContestId()
                + "&main_id=" + guessGame.getMainId()
                + "&detail_id=" + voteTeam.getTeamId()
                + "&count=" + biliProperties.getGuessCoin()
                + "&is_fav=1"
                + "&csrf=" + biliUserData.getBiliJct();
        JSONObject post = biliHttpUtils.post("https://api.bilibili.com/x/esports/guess/add", body);
        if (post.getString("code").equals("0")){
            LOGGER.info("在{}中投给{}{}个硬币",guessGame.getTitle(),voteTeam.getTeamName(),biliProperties.getGuessCoin());
            biliUserData.info("在"+guessGame.getTitle()+"中投给"+voteTeam.getTeamName()+biliProperties.getGuessCoin()+"个硬币");
        }else {
            LOGGER.info("在{"+guessGame.getTitle()+"}中竞猜: {}",post.getString("message"));
            biliUserData.info("在「"+guessGame.getTitle()+"」竞猜: {}",post.getString("message"));
        }
    }

    private ArrayList<GuessGame>  getGuessingList(){
        ArrayList<GuessGame> guessGameArrayList = new ArrayList<>();
        JSONObject jsonObject = biliHttpUtils.get("https://api.bilibili.com/x/esports/guess/collection/question?pn=1&ps=50");
        if (jsonObject.getString("code").equals("0")) {
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
            for (Object o : jsonArray) {
                JSONObject json = (JSONObject) o;
                guessGameArrayList.add(new GuessGame(json));
            }
            return  guessGameArrayList;
        }else {
            return null;
        }
    }
}
