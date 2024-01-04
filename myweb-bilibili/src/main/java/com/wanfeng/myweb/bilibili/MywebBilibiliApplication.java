package com.wanfeng.myweb.bilibili;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = "com.wanfeng.myweb.api")
@SpringBootApplication(scanBasePackages = "com.wanfeng.myweb")
@EnableDiscoveryClient
public class MywebBilibiliApplication {

    public static void main(String[] args) {
        SpringApplication.run(MywebBilibiliApplication.class, args);
    }

}
