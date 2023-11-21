package com.wanfeng.myweb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanfeng.myweb.Entity.QuestionEntity;
import com.wanfeng.myweb.mapper.QuestionMapper;
import com.wanfeng.myweb.service.QuestionService;
import com.wanfeng.myweb.vo.QuestionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, QuestionEntity> implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Override
    public List<QuestionVo> getQuestionList() {
        return questionMapper.getQuestionList();
    }
}
