package com.tmall.order.config;

import com.tmall.auth.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

@Data
@ConfigurationProperties(prefix = "tmall.jwt")//读取application.yml里面的配置
public class JwtProperties {
    private String pubKeyPath;// 公钥
    private  String cookieName;//cookie的名字
    private PublicKey publicKey; // 公钥


    //对象一旦实例化后,就应该读取公钥和私钥
    @PostConstruct
    public void init() throws Exception {
        //读取公钥私钥
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
    }

}
