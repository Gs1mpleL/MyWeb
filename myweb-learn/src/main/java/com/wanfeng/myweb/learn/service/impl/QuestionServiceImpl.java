package com.wanfeng.myweb.learn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.myweb.learn.entity.QuestionEntity;
import com.wanfeng.myweb.learn.mapper.QuestionMapper;
import com.wanfeng.myweb.learn.service.QuestionService;
import com.wanfeng.myweb.learn.vo.QuestionVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, QuestionEntity> implements QuestionService {
    @Resource
    private QuestionMapper questionMapper;

    @Override
    public List<QuestionVo> getQuestionList(String subject) {
        if (Objects.equals(subject, "all")) {
            return questionMapper.getQuestionListRand();
        }
        return questionMapper.getQuestionListBySubject(subject);
    }

    @Override
    public List<String> getSubjectList() {
        return questionMapper.getSubjectList();
    }

    @Override
    public List<QuestionVo> getThreeRandQuestionBySubject(String subject) {
        return questionMapper.getThreeRandQuestionBySubject(subject);
    }

    @Override
    public List<QuestionVo> searchByKeywords(String keyword) {
        return questionMapper.searchByKeywords(keyword);
    }
}
