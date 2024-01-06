package com.wanfeng.myweb.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Flux;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 使用Spring Boot单体架构集成swagger时，是通过包路径进行业务分组，然后在前端进行不同模块的展示，而在微服务架构下，单个服务类似于原来业务组；
 * springfox-swagger提供的分组接口是swagger-resource，返回的是分组接口名称、地址等信息；
 * 在Spring Cloud微服务架构下，需要swagger-resource重写接口，由网关的注册中心动态发现所有的微服务文档
 */
@Primary
@Configuration
public class SwaggerResourceConfig implements SwaggerResourcesProvider
{
    @Autowired
    private RouteLocator routeLocator;

    // 网关应用名称
    @Value ("${spring.application.name}")
    private String applicationName;

    //接口地址
    private static final String API_URI = "/v2/api-docs";

    @Override
    public List<SwaggerResource> get() {
        //接口资源列表
        List<SwaggerResource> resources = new ArrayList<>();
        //服务名称列表
        List<String> routeHosts = new ArrayList<>();
        Flux<Route> routes = routeLocator.getRoutes();
        System.out.println(routes);
        // 获取所有可用的应用名称
        routeLocator.getRoutes()
                .filter(route -> route.getUri().getHost() != null)
                .filter(route -> !applicationName.equals(route.getUri().getHost()))
                .subscribe(route -> routeHosts.add(route.getUri().getHost()));
        // 去重，多负载服务只添加一次
        Set<String> existsServer = new HashSet<>();
        routeHosts.forEach(host -> {
            // 拼接url
            String url = "/" + host + API_URI+"?group=1.0";
            //不存在则添加
            if (!existsServer.contains(url)) {
                existsServer.add(url);
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setUrl(url);
                swaggerResource.setName(host);
                resources.add(swaggerResource);
            }
        });
        return resources;
    }
}