package com.tmall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:UploadProperties
 * Author:  Administrator
 * Date:    2019-12-18 16:00
 * Description: 属性
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Data
@ConfigurationProperties(prefix = "tm.upload")
public class UploadProperties {
    private String baseUrl;
    private List<String> allowTypes;
}
