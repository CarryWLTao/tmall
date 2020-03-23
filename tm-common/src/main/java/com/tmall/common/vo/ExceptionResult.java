package com.tmall.common.vo;

import com.tmall.common.enums.ExceptionEnum;
import lombok.Data;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:ExceptionResult
 * Author:  Administrator
 * Date:    2019-12-13 12:46
 * Description: 111
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestamp;

    public ExceptionResult(ExceptionEnum em) {
        this.status=em.getCode();
        this.message=em.getMsg();
        this.timestamp=System.currentTimeMillis();
    }
}
