package com.tmall.order.utils;/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:PayHelper
 * Author:  Administrator
 * Date:    2020-03-18 13:57
 * Description: 支付助手
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.tmall.common.enums.ExceptionEnum;
import com.tmall.common.exception.TmException;
import com.tmall.order.config.PayProperties;
import com.tmall.order.enums.OrderStatusEnum;
import com.tmall.order.enums.PayState;
import com.tmall.order.mapper.OrderMapper;
import com.tmall.order.mapper.OrderStatusMapper;
import com.tmall.order.pojo.Order;
import com.tmall.order.pojo.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.github.wxpay.sdk.WXPayConstants.FAIL;
import static com.github.wxpay.sdk.WXPayConstants.SUCCESS;

/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:PayHelper
 * Author:  Administrator
 * Date:    2020-03-18 13:57
 * Description: 支付助手
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Slf4j
@Component
public class PayHelper {
    @Autowired
    private WXPay wxPay;
    @Autowired
    private PayProperties payProperties;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;

    public String createrOrder(Long orderId, Long totalPay, String desc) {
        try {
            Map<String, String> data = new HashMap<>();
            // 商品描述
            data.put("body", desc);
            // 订单号
            data.put("out_trade_no", orderId.toString());
           /* //货币
            data.put("fee_type", "CNY");*/
            //金额，单位是分
            data.put("total_fee", totalPay.toString());
            //调用微信支付的终端IP（estore商城的IP）
            data.put("spbill_create_ip", "127.0.0.1");
            //回调地址
            data.put("notify_url", payProperties.getNotifyUrl());
            // 交易类型为扫码支付
            data.put("trade_type", "NATIVE");
            //商品id,使用假数据
            data.put("product_id", "1234567");
            //向微信请求下单,返回二维码付款地址
            Map<String, String> result = wxPay.unifiedOrder(data);
            //判断通信和业务标识
            isSuccess(result);
            //下单成功获取支付链接
            String url = result.get("code_url");
            return url;
        } catch (Exception e) {
            log.error("[微信下单]创建预交易订单异常失败", e);
            return null;
        }

    }

    public void isSuccess(Map<String, String> result) {
        //判断通信表示
        String returnCode = result.get("return_code");
        if (FAIL.equals(returnCode)) {
            //通信失败
            log.error("[微信下单] 微信下单通信失败，失败原因:{}", result.get("return_msg"));
            throw new TmException(ExceptionEnum.WX_PAY_ORDER_FAIL);
        }
        //判断业务标识
        String resultCode = result.get("result_code");
        if (FAIL.equals(resultCode)) {
            //通信失败
            log.error("[微信下单] 微信下单通信失败，错误码:{},原因:{}",
                    result.get("err_code"), result.get("err_code_des"));
            throw new TmException(ExceptionEnum.WX_PAY_ORDER_FAIL);
        }
    }

    public void isValidSign(Map<String, String> data) {
        try {
            //重新生成签名
            String sign1 = WXPayUtil.generateSignature(data, payProperties.getKey(), WXPayConstants.SignType.HMACSHA256);
            String sign2 = WXPayUtil.generateSignature(data, payProperties.getKey(), WXPayConstants.SignType.MD5);
            // 和传过来的签名进行比较
            String sign = data.get("sign");
            if (!StringUtils.equals(sign, sign1) && !StringUtils.equals(sign, sign2)) {
                //签名有误
                throw new TmException(ExceptionEnum.INVALID_SIGN_ERROR);
            }
        } catch (Exception e) {
            throw new TmException(ExceptionEnum.INVALID_SIGN_ERROR);
        }


    }

    public PayState queryPayState(Long orderId) {
        try {
            //组织请求参数
            Map<String, String> data = new HashMap<>();
            //订单号
            data.put("out_trade_no", orderId.toString());
            Map<String, String> result = wxPay.orderQuery(data);
            //校验状态
            isSuccess(result);
            //校验签名
            isValidSign(result);
            //校验金额
            String totalFeeStr = result.get("total_fee");
            String tradeNo = result.get("out_trade_no");
            //金额是空 报异常
            if (StringUtils.isEmpty(totalFeeStr) || StringUtils.isEmpty(tradeNo)) {
                throw new TmException(ExceptionEnum.INVALID_ORDER_PARAM);
            }
            //获取结果中的金额
            Long totalFee = Long.valueOf(totalFeeStr);
            //获取订单金额
            Order order = orderMapper.selectByPrimaryKey(orderId);
            if (totalFee != /*order.getActualPay()*/ 1) {
                //金额不符
                throw new TmException(ExceptionEnum.INVALID_ORDER_PARAM);
            }
            /**
             * SUCCESS-支付成功
             * REFUND-转入退款
             * NOTPAY-未支付
             * CLOSED-已关闭
             * REVOKED-已撤销(刷卡支付)
             * USERPAYING-用户支付中
             * PAYERROR-支付失败
             */
            String state = result.get("trade_state");
            if (SUCCESS.equals(state)) {
                //支付成功
                //修改订单状态
                OrderStatus status = new OrderStatus();
                status.setStatus(OrderStatusEnum.PAYED.value());
                status.setOrderId(orderId);
                status.setPaymentTime(new Date());
                int count = orderStatusMapper.updateByPrimaryKeySelective(status);
                if (count != 1) {
                    throw new TmException(ExceptionEnum.UPDATE_ORDER_STATUS_ERROR);
                }
                //返回成功
                return PayState.SUCCESS;
            }
            if ("NOTPAY".equals(state) || "USERPAYING".equals(state)) {
                return PayState.NOT_PAY;
            }
            //支付失败
            return PayState.FAIL;
        } catch (Exception e) {
            return PayState.NOT_PAY;
        }
    }
}
