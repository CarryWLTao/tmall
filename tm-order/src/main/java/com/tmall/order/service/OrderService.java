package com.tmall.order.service;/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:OrderService
 * Author:  Administrator
 * Date:    2020-03-16 16:42
 * Description: 订单业务
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */

import com.tmall.auth.pojo.UserInfo;
import com.tmall.common.dto.CartDTO;
import com.tmall.common.enums.ExceptionEnum;
import com.tmall.common.exception.TmException;
import com.tmall.common.utils.IdWorker;
import com.tmall.item.pojo.Sku;
import com.tmall.order.client.AddressClient;
import com.tmall.order.client.GoodsClient;
import com.tmall.order.dto.AddressDTO;
import com.tmall.order.dto.OrderDTO;
import com.tmall.order.enums.OrderStatusEnum;
import com.tmall.order.enums.PayState;
import com.tmall.order.interceptor.UserInterceptor;
import com.tmall.order.mapper.OrderDetailMapper;
import com.tmall.order.mapper.OrderMapper;
import com.tmall.order.mapper.OrderStatusMapper;
import com.tmall.order.pojo.Order;
import com.tmall.order.pojo.OrderDetail;
import com.tmall.order.pojo.OrderStatus;
import com.tmall.order.utils.PayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:OrderService
 * Author:  Administrator
 * Date:    2020-03-16 16:42
 * Description: 订单业务
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private PayHelper payHelper;

    @Transactional
    public Long createOrder(OrderDTO orderDTO) {
        //新增订单
        Order order = new Order();
        //订单编号的生成
        long orderId = idWorker.nextId();
        //初始化订单信息
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDTO.getPaymentType());

        //获取用户信息
        UserInfo user = UserInterceptor.getUser();
        //初始化订单信息
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        order.setBuyerRate(false);

        //收件人地址
        //获取收件人信息
        AddressDTO addr = AddressClient.findById(orderDTO.getAddressId());
        order.setReceiver(addr.getName());
        order.setReceiverAddress(addr.getAddress());
        order.setReceiverCity(addr.getCity());
        order.setReceiverDistrict(addr.getDistrict());
        order.setReceiverMobile(addr.getPhone());
        order.setReceiverState(addr.getState());
        order.setReceiverZip(addr.getZipCode());

        /**
         * 金额
         * 把CartDto转为一个map,key是sku的id,值是num
         */
        Map<Long, Integer> numMap = orderDTO.getCarts().stream().collect(Collectors.toMap(CartDTO::getSkuId, CartDTO::getNum));
        Set<Long> ids = numMap.keySet();
        //根据id查询所有的sku
        List<Sku> skus = goodsClient.querySkuByIds(new ArrayList<>(ids));
        //准备orderDetail集合
        List<OrderDetail> details = new ArrayList<>();
        long totalPay = 0;
        for (Sku sku : skus) {
            //计算商品总价
            totalPay += sku.getPrice() * numMap.get(sku.getId());
            //封装orderdetail
            OrderDetail detail = new OrderDetail();
            detail.setImage(StringUtils.substringBefore(sku.getImages(), ","));
            detail.setNum(numMap.get(sku.getId()));
            detail.setOrderId(orderId);
            detail.setOwnSpec(sku.getOwnSpec());
            detail.setPrice(sku.getPrice());
            detail.setSkuId(sku.getId());
            detail.setTitle(sku.getTitle());
            details.add(detail);
        }
        order.setTotalPay(totalPay);
        //实付金额：  总金额  +  邮费  - 优惠金额
        order.setActualPay(totalPay + order.getPostFee() - 0);
        //写入数据库
        int count = orderMapper.insertSelective(order);
        if (count != 1) {
            log.error("[创建订单] 创建订单失败,orderId:{}", orderId);
            throw new TmException(ExceptionEnum.CREATE_ORDER_ERROR);
        }

        //新增订单详情
        count = orderDetailMapper.insertList(details);
        if (count != 1) {
            log.error("[创建订单] 创建订单失败,orderId:{}", orderId);
            throw new TmException(ExceptionEnum.CREATE_ORDER_ERROR);
        }
        //新增订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreateTime(order.getCreateTime());
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.UN_PAY.value());
        count = orderStatusMapper.insertSelective(orderStatus);
        if (count != 1) {
            log.error("[创建订单] 创建订单失败,orderId:{}", orderId);
            throw new TmException(ExceptionEnum.CREATE_ORDER_ERROR);
        }
        //减库存
        List<CartDTO> carts = orderDTO.getCarts();
        goodsClient.decreaseStock(carts);
        return orderId;
    }

    public Order queryOrderById(Long id) {
        Order order = orderMapper.selectByPrimaryKey(id);
        if (order == null) {
            //不存在
            throw new TmException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        //查询订单详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(id);
        List<OrderDetail> details = orderDetailMapper.select(detail);
        //订单不存在
        if (CollectionUtils.isEmpty(details)) {
            throw new TmException(ExceptionEnum.ORDER_DETAIL_NOT_FOUND);
        }
        order.setOrderDetails(details);
        //查询订单状态
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(id);
        if (orderStatus == null) {
            //不存在
            throw new TmException(ExceptionEnum.ORDER_STATUS_NOT_FOUND);
        }
        order.setOrderStatus(orderStatus);
        return order;
    }

    public String createPayUrl(Long orderId) {
        //查询订单
        Order order = queryOrderById(orderId);
        //判断订单状态
        Integer status = order.getOrderStatus().getStatus();
        if (status != OrderStatusEnum.UN_PAY.value()) {
            //订单状态异常
            throw new TmException(ExceptionEnum.ORDER_STATUS_ERROR);
        }
        //支付金额
        Long actualPay = order.getActualPay();
        //商品描述
        OrderDetail detail = order.getOrderDetails().get(0);
        String desc = detail.getTitle();
        //返回url
        return payHelper.createrOrder(orderId, actualPay, desc);
    }

    public void handleNotify(Map<String, String> result) {
        //数据的校验
        payHelper.isSuccess(result);

        //校验签名
        payHelper.isValidSign(result);
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
        Long orderId = Long.valueOf(tradeNo);
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (totalFee != /*order.getActualPay()*/ 1) {
            //金额不符
            throw new TmException(ExceptionEnum.INVALID_ORDER_PARAM);
        }
        //修改订单状态
        OrderStatus status = new OrderStatus();
        status.setStatus(OrderStatusEnum.PAYED.value());
        status.setOrderId(orderId);
        status.setPaymentTime(new Date());
        int count = orderStatusMapper.updateByPrimaryKeySelective(status);
        if (count != 1) {
            throw new TmException(ExceptionEnum.UPDATE_ORDER_STATUS_ERROR);
        }
        log.info("【订单回调】,订单支付成功!订单编号:{}", orderId);
    }

    public PayState queryOrderState(Long orderId) {
        //查询订单状态
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        Integer status = orderStatus.getStatus();
        //判断是否支付
        if (status != OrderStatusEnum.UN_PAY.value()) {
            //如果不是未付款 真的是已支付
            return PayState.SUCCESS;
        }
        //如果是未支付 但其实不一定是未支付 必须去微信查询支付状态
        return payHelper.queryPayState(orderId);
    }
}
