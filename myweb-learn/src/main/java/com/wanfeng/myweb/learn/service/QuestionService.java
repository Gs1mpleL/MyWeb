package com.wanfeng.myweb.learn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanfeng.myweb.learn.entity.QuestionEntity;
import com.wanfeng.myweb.learn.vo.QuestionVo;

import java.util.List;

public interface QuestionService extends IService<QuestionEntity> {
    List<QuestionVo> getQuestionList(String subject);

    List<String> getSubjectList();

    List<QuestionVo> getThreeRandQuestionBySubject(String subject);

    List<QuestionVo> searchByKeywords(String keyword);
}
