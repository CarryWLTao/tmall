package com.tmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:TmItemApplication
 * Author:  Administrator
 * Date:    2019-12-12 18:56
 * Description: 商品微服务启动类
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.tmall.item.mapper")
public class TmItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(TmItemApplication.class);
    }
}
