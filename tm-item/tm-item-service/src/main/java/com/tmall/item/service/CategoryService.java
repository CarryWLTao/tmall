package com.tmall.item.service;

import com.tmall.common.enums.ExceptionEnum;
import com.tmall.common.exception.TmException;
import com.tmall.item.mapper.CategoryMapper;
import com.tmall.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:CategoryService
 * Author:  Administrator
 * Date:    2019-12-14 15:31
 * Description: 类目业务
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryListByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> list = categoryMapper.select(category);
        //判断结果 查不到返回404
        if (CollectionUtils.isEmpty(list)){
            throw new TmException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }
    public List<Category> queryByIds(List<Long> ids){
        List<Category> list = categoryMapper.selectByIdList(ids);
        //判断结果 查不到返回404
        if (CollectionUtils.isEmpty(list)){
            throw new TmException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }
}
