package com.wanfeng.myweb.po;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class GuessGame {
    private String title;
    private String contestId;
    private String mainId;
    private GuessTeam guessTeam1;
    private GuessTeam guessTeam2;

    public GuessGame(JSONObject jsonObject) {
        JSONArray questions = jsonObject.getJSONArray("questions");
        JSONObject contest = jsonObject.getJSONObject("contest");
        contestId = contest.getString("id");
        JSONObject jj = (JSONObject)questions.get(0);
        mainId = jj.getString("id");
        title = jj.getString("title");
        JSONArray details = jj.getJSONArray("details");
        JSONObject jjj1 = (JSONObject)details.get(0);
        JSONObject jjj2 = (JSONObject)details.get(1);
        guessTeam1 = new GuessTeam(jjj1.getString("option"), jjj1.getString("detail_id"), jjj1.getString("odds"));
        guessTeam2 = new GuessTeam(jjj2.getString("option"), jjj2.getString("detail_id"), jjj2.getString("odds"));
    }
}