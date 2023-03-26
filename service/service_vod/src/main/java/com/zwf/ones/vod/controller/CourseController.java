package com.zwf.ones.vod.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zwf.ones.model.vod.Course;
import com.zwf.ones.result.Result;
import com.zwf.ones.vo.vod.*;
import com.zwf.ones.vod.service.ChapterService;
import com.zwf.ones.vod.service.CourseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-03-22
 */
@RestController
@RequestMapping("/admin/vod/course")
//@CrossOrigin
public class CourseController {

    @Resource
    private CourseService courseService;

    @Resource
    private ChapterService chapterService;



    //大纲列表
    @ApiOperation("大纲列表")
    @GetMapping("getNestedTreeList/{courseId}")
    public Result getTreeList(@PathVariable Long courseId){
        List<ChapterVo> list=chapterService.getTreeList(courseId);
        return Result.ok(list);
    }

    //根据id查询返回数据
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id){
        CourseFormVo courseFormVo=courseService.geCourseInfoById(id);
        return Result.ok(courseFormVo);
    }

    //修改课程
    @PostMapping("update")
    public Result update(@RequestBody CourseFormVo courseFormVo){
        courseService.updateCourseId(courseFormVo);
        return Result.ok(courseFormVo.getId());
    }

    //添加课程基本信息
    @ApiOperation("添加课程")
    @PostMapping("save")
    public Result save(@RequestBody CourseFormVo courseFormVo){
        Long courseId=courseService.saveCourseInfo(courseFormVo);
        return Result.ok(courseId);
    }

    //点播课程列表
    @ApiOperation("点播课程列表")
    @GetMapping("{page}/{limit}")
    public Result courseResult(@PathVariable Long page,
                               @PathVariable Long limit,
                               CourseQueryVo courseQueryVo){
        Page<Course>pageParam =new Page<>(page,limit);
        Map<String,Object> map=courseService.findPageCourse(pageParam,courseQueryVo);
        return Result.ok(map);


    }


    //根据课程id 查询发布的课程信息
    @ApiOperation("id查询课程发布的信息")
    @GetMapping("getCoursePublishVo/{id}")
    public Result getCoursePublishVo(@PathVariable Long id){
        CoursePublishVo coursePublishVo=courseService.getCoursePublishVo(id);
        return Result.ok(coursePublishVo);
    }

    //课程最终发布
    @ApiOperation("课程最终发布")
    @PutMapping("publishCourse/{id}")
    public Result publishCourse(@PathVariable Long id){
        courseService.publishCourse(id);
        return Result.ok(null);
    }

    //点播课程删除，根据id
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        courseService.removeCourseId(id);
        return Result.ok(null);
    }








}

