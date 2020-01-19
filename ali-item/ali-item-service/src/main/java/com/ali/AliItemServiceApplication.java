package com.ali;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author:wangsusheng
 * @Date: 2020/1/13 17:25
 */
@Transactional
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.ali.item.mapper")
public class AliItemServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(AliItemServiceApplication.class,args);
    }
}
