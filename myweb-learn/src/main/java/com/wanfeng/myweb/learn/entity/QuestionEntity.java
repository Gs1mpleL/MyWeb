package com.wanfeng.myweb.learn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("question_table")
@NoArgsConstructor
public class QuestionEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("subject")
    private String subject;
    @TableField("question")
    private String question;
    @TableField("answer")
    private String answer;

    public QuestionEntity(String subject, String question, String answer) {
        this.subject = subject;
        this.question = question;
        this.answer = answer;
    }
}
