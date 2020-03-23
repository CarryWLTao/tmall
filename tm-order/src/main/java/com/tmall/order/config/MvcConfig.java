package com.tmall.order.config;/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:MvcConfig
 * Author:  Administrator
 * Date:    2020-03-12 15:37
 * Description: 让拦截器生效
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */

import com.tmall.order.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:MvcConfig
 * Author:  Administrator
 * Date:    2020-03-12 15:37
 * Description: 让拦截器生效
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)

public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private JwtProperties prop;
    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor(prop)).addPathPatterns("/order/**");
    }
}
