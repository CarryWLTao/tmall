package com.tmall.item.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:SpecGroup
 * Author:  Administrator
 * Date:    2019-12-24 16:20
 * Description: 规格类
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Data
@Table(name = "tb_spec_group")
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cid;

    private String name;
    // private List<SpecParam> specParams;

    public SpecGroup() {
    }
}