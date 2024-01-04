package com.wanfeng.myweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MywebGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MywebGatewayApplication.class, args);
    }

}
