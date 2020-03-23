package com.tmall.order.enums;/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:OrderStatusEnum
 * Author:  Administrator
 * Date:    2020-03-17 13:30
 * Description: 订单状态枚举
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */

/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:OrderStatusEnum
 * Author:  Administrator
 * Date:    2020-03-17 13:30
 * Description: 订单状态枚举
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
public enum  OrderStatusEnum {

    UN_PAY(1,"未付款"),
    PAYED(2,"已付款,未发货"),
    DELIVERED(3,"已发货,未确认"),
    SUCCESS(4,"已确认,未评价"),
    CLOSED(5,"交易关闭"),
    RATED(6,"已评价"),
    ;
    private int code;//状态码
    private String desc;//说明

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int value(){
        return this.code;
    }
}
