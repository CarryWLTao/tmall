package com.tmall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
@Data
@ConfigurationProperties(prefix = "tencent.sms")
public class TencentSmsSendProperties {
    /**
     *     secretId: AKIDc0CG5eP1qXnohMe0GH1VjiQt8mEQEPM4  # 你自己的secretId
     *     secretKey: cp55rOeeNt9nDMxJRIuFKDp67OTIsBXc # 你自己的secretKey
     *     appid: 1400325960 #应用id
     *     sign: CarryWang工作室  # 签名名称
     *     templateID: 5473 模板id
     */
    String secretId;
    String secretKey;
    String appid;
    String sign;
    String templateID;

}
