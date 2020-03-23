package com.tmall.item.mapper;

import com.tmall.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:BrandMapper
 * Author:  Administrator
 * Date:    2019-12-15 16:04
 * Description: 品牌mapper
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
public interface BrandMapper extends Mapper<Brand> {
    @Insert("insert into tb_category_brand (category_id,brand_id)values(#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Select("SELECT b.* FROM tb_brand b INNER JOIN tb_category_brand cb ON b.id = cb.brand_id WHERE cb.category_id = #{cid}")
    List<Brand> queryByCategoryId(@Param("cid") Long cid);;
}
