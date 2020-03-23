package com.tmall.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:CateGory
 * Author:  CarryWLTao
 * Date:    2019-12-14 15:21
 * Description: 商品类目实体类
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Table(name="tb_category")
@Data
public class Category {
    @Id
    @KeySql(useGeneratedKeys=true)
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;
}
