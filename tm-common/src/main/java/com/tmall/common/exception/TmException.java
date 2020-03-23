package com.tmall.common.exception;

import com.tmall.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:TmException
 * Author:  Administrator
 * Date:    2019-12-13 12:35
 * Description: 异常
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TmException extends RuntimeException{
    private ExceptionEnum exceptionEnum;
}
