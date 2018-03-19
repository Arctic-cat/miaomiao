package com.miaomiao.service.impl;

import com.google.common.collect.Lists;
import com.miaomiao.service.IFileService;
import com.miaomiao.util.FTPUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        //扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);

        String uploadFileName = UUID.randomUUID() + "." + fileExtensionName;
        logger.info("开始上传文件,文件名:{},上传的路径:{},新文件名{}",fileName,path,uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);
            //文件上传到tomcat成功
            //将targetFile上传到ftp服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //删除upload下的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }
        return uploadFileName;

    }
}
