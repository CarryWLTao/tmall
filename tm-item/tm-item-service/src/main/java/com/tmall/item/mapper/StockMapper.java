package com.tmall.item.mapper;

import com.tmall.common.mapper.BaseMapper;
import com.tmall.item.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


/**
 * Copyright(C),2019-2020,CarryWLTao互联网工作室
 * FileName:StockMapper
 * Author:  Administrator
 * Date:    2020-01-02 16:21
 * Description: stock
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
public interface StockMapper extends BaseMapper<Stock> {
    @Update("UPDATE tb_stock SET stock= stock - #{num} WHERE sku_id= #{id} AND stock >= #{num}")
    int decreaseStock(@Param("id") Long id,@Param("num") Integer num);
}
