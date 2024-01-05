package com.wanfeng.myweb.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.wanfeng.myweb.user","com.wanfeng.myweb.common"})
@EnableDiscoveryClient
public class MywebUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MywebUserApplication.class, args);
    }

}
