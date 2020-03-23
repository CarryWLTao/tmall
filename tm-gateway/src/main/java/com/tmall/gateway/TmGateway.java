package com.tmall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:TmGateway
 * Author:  Administrator
 * Date:    2019-12-12 17:19
 * Description: 网关启动类
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@EnableZuulProxy
@SpringCloudApplication
public class TmGateway {
    public static void main(String[] args) {
        SpringApplication.run(TmGateway.class);
    }
}
