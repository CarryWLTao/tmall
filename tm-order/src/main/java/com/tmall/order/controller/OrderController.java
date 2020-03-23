package com.tmall.order.controller;/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:OrderController
 * Author:  Administrator
 * Date:    2020-03-16 16:39
 * Description: 订单支付等
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */

import com.tmall.order.dto.OrderDTO;
import com.tmall.order.enums.PayState;
import com.tmall.order.pojo.Order;
import com.tmall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:OrderController
 * Author:  Administrator
 * Date:    2020-03-16 16:39
 * Description: 订单支付等
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    /**
     * 创建订单
     * @param orderDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderDTO orderDTO){
        //创建订单
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }
    @GetMapping("{id}")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id") Long id){
        return ResponseEntity.ok(orderService.queryOrderById(id));
    }

    /**
     * 创建支付链接
     * @param orderId
     * @return
     */
    @GetMapping("/url/{id}")
    public ResponseEntity<String> createPayUrl(@PathVariable("id")Long orderId){
        return  ResponseEntity.ok(orderService.createPayUrl(orderId));
    }
    @GetMapping("/state/{id}")
    public ResponseEntity<PayState> queryOrderState(@PathVariable("id")Long orderId){
        return ResponseEntity.ok(orderService.queryOrderState(orderId));
    }
}
