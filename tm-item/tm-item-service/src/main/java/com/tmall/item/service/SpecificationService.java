package com.tmall.item.service;

import com.tmall.common.enums.ExceptionEnum;
import com.tmall.common.exception.TmException;
import com.tmall.item.mapper.SpecGroupMapper;
import com.tmall.item.mapper.SpecParamMapper;
import com.tmall.item.pojo.SpecGroup;
import com.tmall.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:SpecificationService
 * Author:  Administrator
 * Date:    2019-12-24 16:44
 * Description: 业务
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;
    public List<SpecGroup> queryGroupByCid(Long cid) {
        //查询条件
        SpecGroup group=new SpecGroup();
        group.setCid(cid);
        //查询
        List<SpecGroup> list = specGroupMapper.select(group);
        if (CollectionUtils.isEmpty(list)){
            //没查到
            throw new TmException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return list;
    }

    public List<SpecParam> queryParamList(Long gid,Long cid,Boolean searching) {
        SpecParam specParam=new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        //查询
        List<SpecParam> list = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(list)){
            //没查到
            throw new TmException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return list;
    }
}
