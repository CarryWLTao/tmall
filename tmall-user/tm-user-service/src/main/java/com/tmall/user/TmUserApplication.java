package com.tmall.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.tmall.user.mapper")
public class TmUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(TmUserApplication.class, args);
    }
}
