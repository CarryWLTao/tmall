package com.tmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:TmUpLoadApplication
 * Author:  Administrator
 * Date:    2019-12-16 15:11
 * Description: 图片上传微服务启动类
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@EnableDiscoveryClient
@SpringBootApplication
public class TmUpLoadApplication {
    public static void main(String[] args) {
        SpringApplication.run(TmUpLoadApplication.class);
    }
}
