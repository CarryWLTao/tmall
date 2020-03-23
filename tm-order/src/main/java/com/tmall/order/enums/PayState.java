package com.tmall.order.enums;/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:PayState
 * Author:  Administrator
 * Date:    2020-03-20 09:20
 * Description: 支付状态
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */

/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:PayState
 * Author:  Administrator
 * Date:    2020-03-20 09:20
 * Description: 支付状态
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
public enum PayState {

    NOT_PAY(0),
    SUCCESS(1),
    FAIL(2),
    ;
    PayState(int value) {
        this.value = value;
    }
    int value;
    public int getValue(){
        return value;
    }
}
