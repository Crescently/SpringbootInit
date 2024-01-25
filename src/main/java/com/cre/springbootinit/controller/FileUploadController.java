package com.cre.springbootinit.controller;


import com.cre.springbootinit.pojo.common.BaseResponse;
import com.cre.springbootinit.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {


    /**
     * 文件上传
     *
     * @param multipartFile 文件
     */
    @PostMapping("/upload")
    public BaseResponse<String> upload(@RequestParam(value = "file") MultipartFile multipartFile) throws IOException {
        // 获取文件名
        String filename = multipartFile.getOriginalFilename();
        // 保证文件名唯一 ——使用UUID
        assert filename != null;
        filename = UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
        String fileUrl = AliOssUtil.uploadFile(filename, multipartFile.getInputStream());
        return BaseResponse.success(fileUrl);
    }
}
