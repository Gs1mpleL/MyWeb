package com.wanfeng.myweb.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("question_table")
public class QuestionEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    @TableField("subject")
    private String subject;
    @TableField("question")
    private String question;
    @TableField("answer")
    private String answer;
}
