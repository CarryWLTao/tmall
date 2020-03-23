package com.tmall.mq;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.tmall.config.SmsProperties;
import com.tmall.config.TencentSmsSendProperties;
import com.tmall.utils.SmsUtils;
import com.tmall.utils.TencentSendSmsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
@Slf4j
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {

    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private TencentSendSmsUtils tencentSendSmsUtils;
    @Autowired
    private SmsProperties prop;
    @Autowired
    private TencentSmsSendProperties tprop;

    /**
     * 发送短信验证码
     * @param msg
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "tmall.sms.queue", durable = "true"),
            exchange = @Exchange(value = "tmall.sms.exchange",
                                 ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}))
    public void listenSms(Map<String, String> msg) throws Exception {
        if (CollectionUtils.isEmpty(msg)) {
            // 放弃处理
            return;
        }
       String phone = msg.get("phone");
        String[] phoneNumber = phone.split(",");
        //String phone = msg.remove("phone");
       //String code = msg.get("code");
        String templateParamSet = msg.get("templateParamSet");
        String[] templateParam = templateParamSet.split(",");
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(templateParamSet)) {
            // 放弃处理
            return; }
        // 发送消息
      //  smsUtils.sendSms(phone, code, prop.getSignName(),prop.getVerifyCodeTemplate());
        tencentSendSmsUtils.sendSms(tprop.getAppid(),phoneNumber,tprop.getSign()
        ,tprop.getTemplateID(),templateParam);
        //发送短信日志
        log.info("【短信服务】,发送短信验证码，手机号：{}",phone);
    }
}