package com.tmall.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tmall.common.enums.ExceptionEnum;
import com.tmall.common.exception.TmException;
import com.tmall.common.vo.PageResult;
import com.tmall.item.mapper.BrandMapper;
import com.tmall.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:BrandService
 * Author:  Administrator
 * Date:    2019-12-15 16:05
 * Description: 品牌业务类
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            //创建where条件
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        //查询
        List<Brand> list = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            throw new TmException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //解析分页结果
        PageInfo<Brand> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), list);
    }
    //添加事务
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌
        brand.setId(null);
        System.out.println(brand.getName());
        int count = brandMapper.insert(brand);

        if (count != 1){
            throw new TmException((ExceptionEnum.BRAND_SAVE_ERROR));
        }
        //新增中间表
        for(Long cid: cids){
             count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if (count != 1){
                throw new TmException((ExceptionEnum.BRAND_SAVE_ERROR));
            }
        }

    }
    public Brand queryById(Long id){
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand ==null){
            throw new TmException((ExceptionEnum.BRAND_SAVE_ERROR));
        }
        return brand;
    }

    public List<Brand> queryBrandByCid(Long cid) {

        List<Brand> list = brandMapper.queryByCategoryId(cid);
        if (CollectionUtils.isEmpty(list)){
            throw new TmException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return list;
    }
}
