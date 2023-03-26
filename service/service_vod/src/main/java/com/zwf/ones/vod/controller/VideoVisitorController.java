package com.zwf.ones.vod.controller;


import com.zwf.ones.result.Result;
import com.zwf.ones.vod.service.VideoVisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-03-23
 */
@RestController
@RequestMapping("/admin/vod/videoVisitor")
//@CrossOrigin //解决跨域问题
public class VideoVisitorController {
    @Autowired
    private VideoVisitorService videoVisitorService;

    //课程统计接口
    @GetMapping("findCount/{courseId}/{startDate}/{endDate}")
    public Result findCount(@PathVariable Long courseId,
                            @PathVariable String startDate,
                            @PathVariable String endDate
    ){
        Map<String,Object> map =videoVisitorService.findCount(courseId,startDate,endDate);
        return Result.ok(map);

    }

}

