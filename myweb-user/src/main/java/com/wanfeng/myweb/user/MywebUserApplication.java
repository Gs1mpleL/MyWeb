package com.wanfeng.myweb.user;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.wanfeng.myweb.user","com.wanfeng.myweb.common"})
@EnableDiscoveryClient
@EnableKnife4j
public class MywebUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MywebUserApplication.class, args);
    }

}
