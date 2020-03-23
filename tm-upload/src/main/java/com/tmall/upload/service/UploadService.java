package com.tmall.upload.service;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.netflix.discovery.converters.Auto;
import com.tmall.common.enums.ExceptionEnum;
import com.tmall.common.exception.TmException;
import com.tmall.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright(C),2019-2019,CarryWLTao互联网工作室
 * FileName:UploadService
 * Author:  Administrator
 * Date:    2019-12-16 15:21
 * Description: 业务类
 * Version:    1.0
 * History:
 * <author>     <time>      <version>       <desc>
 * 作者姓名     修改时间       版本号          描述
 */
@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {
    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private UploadProperties prop;
   // private static final List<String> ALLOW_TYPES= Arrays.asList("image/jpeg","image/png","image/bmp");
    public String upLoadImage(MultipartFile file) {
        try {
            //校验文件类型
            String contentType = file.getContentType();
            if (!prop.getAllowTypes().contains(contentType)) {
                throw new TmException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new TmException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //准备目标路径
            File dest = new File("F:/IdeaProject/leyou/upload/" + file.getOriginalFilename());
            //保存文件到本地
            file.transferTo(dest);
            //返回路径
           return prop.getBaseUrl() + file.getOriginalFilename();
            //return  "F:/IdeaProject/leyou/upload/" + file.getOriginalFilename();
        } catch (IOException e) {
            log.error("【文件上传】上传文件失败" + e);
            throw new TmException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }

    }
}
