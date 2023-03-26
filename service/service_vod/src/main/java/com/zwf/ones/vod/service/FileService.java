package com.zwf.ones.vod.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    //1.文件上传
    String upload(MultipartFile file);
}
