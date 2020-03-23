package com.tmall.order.controller;/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:NotifyController
 * Author:  Administrator
 * Date:    2020-03-19 16:19
 * Description: 内网穿透
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */

import com.tmall.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:NotifyController
 * Author:  Administrator
 * Date:    2020-03-19 16:19
 * Description: 内网穿透
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Slf4j
@RestController
@RequestMapping("notify")
public class NotifyController {
    @Autowired
    private OrderService orderService;

    /**
     * 微信支付的成功回调
     *
     * @param result
     * @return
     */
    @PostMapping(value = "wxpay", produces = "application/xml")
    // @ResponseBody//  把响应结果转成序列化以后放到响应集里面去
    //@RequestBody 把请求体转化成对象
    public Map<String, String> handleNotify(@RequestBody Map<String, String> result) {
        //处理回调
        orderService.handleNotify(result);

        //日志
        log.info("【支付回调】接收微信支付回调,结果:{}",result);
        //返回成功
        Map<String, String> msg = new HashMap<>();
        msg.put("return_code", "SUCCESS");
        msg.put("return_msg", "OK");
        return msg;
    }
}
