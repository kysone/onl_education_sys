package com.zwf.ones.vod.controller;


import com.zwf.ones.result.Result;
import com.zwf.ones.vod.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "文件上传接口")
@RequestMapping("/admin/vod/file")
//@CrossOrigin
public class FileUploadController {

    @Autowired
    private FileService fileService;

    @ApiOperation(value ="文件上传")
    @PostMapping("upload")
    public Result upload(
                         @ApiParam(name="file",value = "文件",required = true)
                         @RequestParam("file")MultipartFile file){


        String upload = fileService.upload(file);

        return Result.ok(upload).message("文件上传成功");

    }
}
