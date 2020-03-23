package com.tmall.common.vo;

import lombok.Data;

import java.util.List;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:PageResult
 * Author:  Administrator
 * Date:    2019-12-15 16:14
 * Description: 分页结果
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Data
public class PageResult<T> {
    private Long total;// 总条数
    private Long totalPage;// 总页数
    private List<T> items;// 当前页数据

    public PageResult() {
    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Long totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}
