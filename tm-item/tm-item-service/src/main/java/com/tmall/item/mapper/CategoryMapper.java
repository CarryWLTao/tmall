package com.tmall.item.mapper;

import com.tmall.item.pojo.Category;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:CategoryMapper
 * Author:  Administrator
 * Date:    2019-12-14 15:30
 * Description: 商品类目mapper
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long> {
}
