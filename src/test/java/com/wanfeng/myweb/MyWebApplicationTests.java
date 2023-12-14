package com.wanfeng.myweb;

import com.wanfeng.myweb.service.PushService;
import com.wanfeng.myweb.service.QuestionService;
import com.wanfeng.myweb.vo.PushVO;
import com.wanfeng.myweb.vo.QuestionVo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class MyWebApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyWebApplicationTests.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    PushService pushService;
    @Test
    void test() throws Exception {
        List<List<QuestionVo>> allList = new ArrayList<>();
        List<QuestionVo> list1 = questionService.getThreeRandQuestionBySubject("肖八第一套");
        List<QuestionVo> list2 = questionService.getThreeRandQuestionBySubject("肖八第二套");
        List<QuestionVo> list3 = questionService.getThreeRandQuestionBySubject("肖八第三套");
        List<QuestionVo> list4 = questionService.getThreeRandQuestionBySubject("肖八第四套");
        List<QuestionVo> list5 = questionService.getThreeRandQuestionBySubject("肖八第五套");
        List<QuestionVo> list6 = questionService.getThreeRandQuestionBySubject("肖八第六套");
        List<QuestionVo> list7 = questionService.getThreeRandQuestionBySubject("肖八第七套");
        List<QuestionVo> list8 = questionService.getThreeRandQuestionBySubject("肖八第八套");
        List<QuestionVo> list9 = questionService.getThreeRandQuestionBySubject("肖四第一套");
        List<QuestionVo> list10 = questionService.getThreeRandQuestionBySubject("肖四第二套");
        List<QuestionVo> list11 = questionService.getThreeRandQuestionBySubject("肖四第三套");
        List<QuestionVo> list12 = questionService.getThreeRandQuestionBySubject("肖四第四套");
        allList.add(list1);
        allList.add(list2);
        allList.add(list3);
        allList.add(list4);
        allList.add(list5);
        allList.add(list6);
        allList.add(list7);
        allList.add(list8);
        allList.add(list9);
        allList.add(list10);
        allList.add(list11);
        allList.add(list12);
        StringBuilder sb = new StringBuilder();
        for (List<QuestionVo> list : allList) {
            if (!list.isEmpty()){
                for (QuestionVo questionVo : list) {
                    sb.append(questionVo.getQuestion()).append("\n");
                }
            }
        }
        pushService.pushIphone(new PushVO("测试",sb.toString(),"测试"));
    }
}
