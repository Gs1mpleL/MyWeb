package com.wanfeng.myweb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanfeng.myweb.Entity.QuestionEntity;
import com.wanfeng.myweb.vo.QuestionVo;

import java.util.List;

public interface QuestionService extends IService<QuestionEntity> {
    List<QuestionVo> getQuestionList(String subject);

    List<String> getSubjectList();

    List<QuestionVo> getOneQuestion(String subject);

    List<QuestionVo> searchByKeywords(String keyword);
}
