package com.wanfeng.myweb.learn.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanfeng.myweb.learn.entity.QuestionEntity;
import com.wanfeng.myweb.learn.vo.QuestionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper extends BaseMapper<QuestionEntity> {
    //    @Select("SELECT * FROM question_table where subject=#{subject} ORDER BY RAND()")
    @Select("SELECT * FROM question_table where subject=#{subject} LIMIT 50")
    List<QuestionVo> getQuestionListBySubject(String subject);

    @Select("SELECT * FROM question_table ORDER BY RAND() LIMIT 5")
    List<QuestionVo> getQuestionListRand();

    @Select("SELECT DISTINCT subject FROM question_table")
    List<String> getSubjectList();

    @Select("SELECT * FROM question_table where subject=#{subject} ORDER BY RAND() LIMIT 3")
    List<QuestionVo> getThreeRandQuestionBySubject(String subject);

    @Select("SELECT * FROM question_table where question LIKE CONCAT('%', #{keyword}, '%') OR answer LIKE CONCAT('%', #{keyword}, '%')")
    List<QuestionVo> searchByKeywords(String keyword);
}
