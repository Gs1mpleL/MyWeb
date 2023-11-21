package com.wanfeng.myweb.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionVo {
    private int id;
    private String subject;
    private String question;
    private String answer;
}
