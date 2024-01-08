package com.wanfeng.myweb.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MywebJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(MywebJobApplication.class, args);
    }

}
