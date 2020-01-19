package com.ali;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author:wangsusheng
 * @Date: 2020/1/16 11:36
 */

@SpringBootApplication
@EnableDiscoveryClient
public class AliUploadApplication
{
    public static void main(String[] args) {
        SpringApplication.run(AliUploadApplication.class, args);
    }
}
