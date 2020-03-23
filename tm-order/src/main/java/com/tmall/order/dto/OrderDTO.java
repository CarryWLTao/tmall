package com.tmall.order.dto;/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:OrderDTO
 * Author:  Administrator
 * Date:    2020-03-16 16:25
 * Description: 传输数据
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */

import com.tmall.common.dto.CartDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:OrderDTO
 * Author:  Administrator
 * Date:    2020-03-16 16:25
 * Description: 传输数据
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @NotNull
    private Long addressId;//收货人地址id

    private Integer paymentType;//付款类型

    private List<CartDTO> carts;//订单详情
}
