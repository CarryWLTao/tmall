package com.tmall.order.config;/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:WXPayConfiguration
 * Author:  Administrator
 * Date:    2020-03-18 11:33
 * Description: 微信支付
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:WXPayConfiguration
 * Author:  Administrator
 * Date:    2020-03-18 11:33
 * Description: 微信支付
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Configuration
public class WXPayConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "tmall.pay")
    public PayProperties payProperties(){
        return new PayProperties();
    }
    @Bean
    public WXPay wxPay(PayProperties payProperties){
        return new WXPay(payProperties, WXPayConstants.SignType.HMACSHA256);
    }
}
