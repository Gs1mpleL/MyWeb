package com.wanfeng.myweb.crawler.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;

@Data
public class GuessGame {
    private String title;
    private String contestId;
    private String mainId;
    private ArrayList<GuessTeam> guessOptions;
    private String endTime;

    public GuessGame(JSONObject jsonObject) {
        guessOptions = new ArrayList<>();
        JSONArray questions = jsonObject.getJSONArray("questions");
        JSONObject contest = jsonObject.getJSONObject("contest");
        contestId = contest.getString("id");
        JSONObject jj = (JSONObject) questions.get(0);
        mainId = jj.getString("id");
        title = jj.getString("title");
        JSONArray details = jj.getJSONArray("details");
        for (Object detail : details) {
            JSONObject jjj = (JSONObject) detail;
            guessOptions.add(new GuessTeam(jjj.getString("option"), jjj.getString("detail_id"), jjj.getString("odds")));
        }
        endTime = contest.getString("end_time");
    }
}