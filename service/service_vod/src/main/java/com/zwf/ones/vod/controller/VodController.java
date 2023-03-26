package com.zwf.ones.vod.controller;


import com.zwf.ones.result.Result;
import com.zwf.ones.vod.service.VodService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "腾讯云点播")
@RestController
@RequestMapping("/admin/vod")
//@CrossOrigin
public class VodController {


    @Autowired
    private VodService vodService;

    @PostMapping("upload")
    public Result upload(){
        String filrId=vodService.updateVideo();
        return Result.ok(filrId);
    }

    /**
     * 删除腾讯云视频
     */
    @DeleteMapping("remove/{fieldId}")
    public Result remove(@PathVariable String fieldId){
        vodService.removeVideo(fieldId);
        return  Result.ok(null);

    }


}
