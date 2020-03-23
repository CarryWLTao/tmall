package com.tmall.item.controller;

import com.tmall.item.pojo.SpecGroup;
import com.tmall.item.pojo.SpecParam;
import com.tmall.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:SpecificationController
 * Author:  Administrator
 * Date:    2019-12-24 16:44
 * Description: 规格控制类
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@RestController
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据分类id查询规格组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid")Long cid){
        return ResponseEntity.ok(specificationService.queryGroupByCid(cid));
    }

    /**
     * 根据组id查询参数
     * @param gid
     * @param cid
     * @param searching
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamList(@RequestParam(value = "gid",required = false)Long gid,
                                                           @RequestParam(value = "cid",required = false)Long cid,
                                                           @RequestParam(value = "searching",required = false)Boolean searching){
        return ResponseEntity.ok(specificationService.queryParamList(gid,cid,searching));
    }
}
