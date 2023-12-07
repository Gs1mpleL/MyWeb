package com.wanfeng.myweb;

import com.wanfeng.myweb.Entity.QuestionEntity;
import com.wanfeng.myweb.quartz.DailyJob;
import com.wanfeng.myweb.service.BiliService;
import com.wanfeng.myweb.service.QuestionService;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class MyWebApplicationTests {
    @Autowired
    private QuestionService questionService;
}
