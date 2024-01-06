package com.wanfeng.myweb.crawler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GuessTeam {
    private String teamName;
    private String teamId;
    private String teamRate;
}
