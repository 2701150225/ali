package com.ali.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Author:wangsusheng
 * @Date: 2020/1/13 16:31
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class AliGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(AliGatewayApplication.class, args);
    }
}
