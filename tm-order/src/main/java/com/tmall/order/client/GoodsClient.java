package com.tmall.order.client;/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:GoodsClient
 * Author:  Administrator
 * Date:    2020-03-17 11:03
 * Description: 商品
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */

import com.tmall.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:GoodsClient
 * Author:  Administrator
 * Date:    2020-03-17 11:03
 * Description: 商品
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
