package com.tmall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient//注册到eureka上
public class TmCartApplication {
    public static void main(String[] args) {
        SpringApplication.run(TmCartApplication.class,args);
    }
}
