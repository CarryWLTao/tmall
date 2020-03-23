package com.tmall.order.config;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;

@Data
public class PayProperties implements WXPayConfig {
    /**
     * 读取配置文件的配置类
     */
    @Autowired
    private PayProperties payProperties;
    private String appId; // 公众账号ID
    private String mchId; // 商户号
    private String key; // 生成签名的密钥
    private int connectTimeoutMs; // 连接超时时间
    private int readTimeoutMs;// 读取超时时间
    private String notifyUrl;   //微信支付成功后微信回调的自己后台的接口


    @Override
    public String getAppID() {
        return payProperties.getAppID();
    }

    @Override
    public String getMchID() {
        return payProperties.getMchID();
    }
    @Override
    public String getKey() {
        return payProperties.getKey();
    }
    @Override
    public InputStream getCertStream() {
        return null;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return payProperties.getConnectTimeoutMs();
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return payProperties.getReadTimeoutMs();
    }
}