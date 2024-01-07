package com.wanfeng.myweb.learn.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionVo {
    private Integer id;
    private String subject;
    private String question;
    private String answer;
}
