package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.Entity.QuestionEntity;
import com.wanfeng.myweb.service.QuestionService;
import com.wanfeng.myweb.vo.QuestionVo;
import com.wanfeng.myweb.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/getQuestionList")
    public Result<List<QuestionVo>> getQuestionList(){
        return Result.ok(questionService.getQuestionList());
    }

    @PostMapping("/addQuestion")
    public Result<?> addQuestion(@RequestBody QuestionVo questionVo){
        QuestionEntity questionEntity = new QuestionEntity();
        BeanUtils.copyProperties(questionVo,questionEntity);
        return Result.ok(questionService.save(questionEntity));
    }
}
