package com.tmall.order;/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:TmOrderApplication
 * Author:  Administrator
 * Date:    2020-03-13 15:37
 * Description: 订单微服务的启动类
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:TmOrderApplication
 * Author:  Administrator
 * Date:    2020-03-13 15:37
 * Description: 订单微服务的启动类
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.tmall.order.mapper")
public class TmOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(TmOrderApplication.class,args);
    }
}
