package com.wanfeng.myweb.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.wanfeng.myweb.api")
@SpringBootApplication(scanBasePackages = "com.wanfeng.myweb")
@EnableDiscoveryClient
public class MywebCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MywebCrawlerApplication.class, args);
    }

}
