package com.wanfeng.myweb;

import com.wanfeng.myweb.service.biliTask.CompetitionGuessTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyWebApplicationTests {

    @Autowired
    private CompetitionGuessTask competitionGuessTask;
    @Test
    void contextLoads() {
        competitionGuessTask.run();
    }

}
