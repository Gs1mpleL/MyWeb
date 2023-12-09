package com.wanfeng.myweb.controller;

import com.wanfeng.myweb.Entity.QuestionEntity;
import com.wanfeng.myweb.service.QuestionService;
import com.wanfeng.myweb.vo.QuestionVo;
import com.wanfeng.myweb.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/getQuestionList/{subject}")
    public Result<List<QuestionVo>> getQuestionList(@PathVariable String subject) {
        return Result.ok(questionService.getQuestionList(subject));
    }

    @GetMapping("/getOneQuestion/{subject}")
    public Result<List<QuestionVo>> getOneQuestion(@PathVariable String subject) {
        return Result.ok(questionService.getOneQuestion(subject));
    }

    @GetMapping("/searchByKeywords/{keyword}")
    public Result<List<QuestionVo>> searchByKeywords(@PathVariable String keyword) {
        return Result.ok(questionService.searchByKeywords(keyword));
    }

    @PostMapping("/addQuestion")
    public Result<?> addQuestion(@RequestBody QuestionVo questionVo) {
        QuestionEntity questionEntity = new QuestionEntity();
        BeanUtils.copyProperties(questionVo, questionEntity);
        return Result.ok(questionService.save(questionEntity));
    }

    @GetMapping("/getSubjectList")
    public Result<?> getSubjectList() {
        return Result.ok(questionService.getSubjectList());
    }

    @PostMapping("deleteQuestion/{id}")
    public Result<?> deleteQuestion(@PathVariable Integer id) {
        return Result.ok(questionService.removeById(id));
    }

    @PostMapping("/updateQuestion")
    public Result<?> updateQuestion(@RequestBody QuestionVo questionVo) {
        System.out.println(questionVo);
        QuestionEntity questionEntity = new QuestionEntity();
        BeanUtils.copyProperties(questionVo, questionEntity);
        return Result.ok(questionService.updateById(questionEntity));
    }
}
