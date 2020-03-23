package com.tmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:TmRegistry
 * Author:  CarryW
 * Date:    2019-12-12 17:11
 * Description: 注册中心启动类
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@EnableEurekaServer
@SpringBootApplication
public class TmRegistry {
    public static void main(String[] args) {
        SpringApplication.run(TmRegistry.class);
    }
}
