package com.tmall.common.advice;

import com.tmall.common.enums.ExceptionEnum;
import com.tmall.common.exception.TmException;
import com.tmall.common.vo.ExceptionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:CommonException
 * Author:  Administrator
 * Date:    2019-12-13 12:11
 * Description: 通用异常处理
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@ControllerAdvice //拦截所有Controller
public class CommonExceptionHandler {
    @ExceptionHandler(TmException.class)
    public ResponseEntity<ExceptionResult> handlerException(TmException e) {
        ExceptionEnum em = e.getExceptionEnum();
        return ResponseEntity.status(e.getExceptionEnum().getCode()).body(new ExceptionResult(e.getExceptionEnum()));
    }
}
