package com.ali.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @Author:wangsusheng
 * @Date: 2020/1/16 11:36
 **/
@Service
public class UploadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    //媒体文件类型白名单
    private static final List<String> CONTENT_TYPE = Arrays.asList("image/gif", "image/jpeg");

    @Autowired
    private FastFileStorageClient storageClient;

    public String uploadImage(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        //检验文件类型，是否是图片
        String contentType = file.getContentType();
        //判断是否包含
        if (!CONTENT_TYPE.contains(contentType)) {
            //大括号在这里是一个占位符，请注意
            LOGGER.info("文件类型不合法：{}", originalFilename);
            return null;
        }

        try {
            //检验文件内容，使用ImageIO来读取文件，看是否能读取到流
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                LOGGER.info("文件内容不合法：{}", originalFilename);
                return null;
            }

            //保存到文件服务器
           // file.transferTo(new File("E:\\BaiduNetdiskDownload\\乐优商城-11月版\\leyou" + originalFilename));

            //返回url，进行回显
          // return "http://image.leyou.com/" + originalFilename;


            //使用fastDFS来保存图片
            //获取文件后缀名
            String ext = StringUtils.substringAfterLast(originalFilename, ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);
            //返回url，注意使用带分组的路径
           return "http://image.wangkun.xyz/" + storePath.getFullPath();
        } catch (Exception e) {
            LOGGER.info("服务器内部错误：{}", originalFilename);
            e.printStackTrace();
        }
        return null;
    }
}
