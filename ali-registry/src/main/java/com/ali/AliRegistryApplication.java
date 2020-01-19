package com.ali;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Author:wangsusheng
 * @Date: 2020/1/13 17:50
 */
@SpringBootApplication
@EnableEurekaServer
public class AliRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(AliRegistryApplication.class,args);
    }
}
