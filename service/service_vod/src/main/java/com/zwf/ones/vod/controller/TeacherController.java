package com.zwf.ones.vod.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zwf.ones.model.vod.Teacher;
import com.zwf.ones.result.Result;
import com.zwf.ones.vo.vod.TeacherQueryVo;
import com.zwf.ones.vod.service.TeacherService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2023-03-19
 */
@RestController
@RequestMapping("/admin/vod/teacher")
//@CrossOrigin
public class TeacherController {


    @Autowired
    private TeacherService teacherService;

    @GetMapping("findAll")
    public Result findAllTeacher(){

    /*    try {
            int i=10/0;
        } catch (Exception e) {
           throw new OnesExceptionHandler(20001,"出现自定义异常");
        }*/
        List<Teacher> list = teacherService.list();
        return Result.ok(list).message("查询成功");
    }

    //remove/1
    //2 逻辑删除讲师
    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("remove/{id}")
    public Result removeTeacher(@ApiParam(name="id",value = "ID",required = true)
                                @PathVariable long id){
        boolean isSuccess=teacherService.removeById(id);
        if(isSuccess){
            return Result.ok(null);
        }else{
            return Result.fail(null);
        }
    }


    @ApiOperation("条件查询分页")
    @PostMapping ("findQueryPage/{current}/{limit}")
    public Result findPage(@PathVariable long current,
                           @PathVariable long limit,
                           @RequestBody(required = false) TeacherQueryVo teacherQueryVo){
        Page<Teacher> pageParm=new Page<>(current,limit);

        if(teacherQueryVo==null){
            Page<Teacher> pageModel = teacherService.page(pageParm, null);
            return Result.ok(pageModel);
        }else {
            String name = teacherQueryVo.getName();
            Integer level = teacherQueryVo.getLevel();
            String joinDateBegin = teacherQueryVo.getJoinDateBegin();
            String joinDateEnd = teacherQueryVo.getJoinDateEnd();

            QueryWrapper<Teacher> wrapper=new QueryWrapper<>();

            if (!StringUtils.isEmpty(name)){
                wrapper.like("name",name);
            }
            if (!StringUtils.isEmpty(level)){
                wrapper.eq("level",level);
            }
            if(!StringUtils.isEmpty(joinDateBegin)){
                wrapper.ge("join_date",joinDateBegin);
            }
            if(!StringUtils.isEmpty(joinDateEnd)){
                wrapper.le("join_date",joinDateEnd);
            }

            Page<Teacher> pageModel = teacherService.page(pageParm, wrapper);

            return Result.ok(pageModel);
        }

    }


    //4、添加操作
    @ApiOperation("添加讲师")
    @PostMapping("saveTeacher")
    public Result saveTeacher(@RequestBody Teacher teacher ){
        boolean save = teacherService.save(teacher);
        if (save){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }


    //5、修改操作-根据id查询
    @ApiOperation("根据id查询")
    @GetMapping("getTeacher/{id}")
    public Result getTeacher(@PathVariable Long id){
        Teacher teacher = teacherService.getById(id);
        return Result.ok(teacher);

    }

    //6、修改-最终实现
    @ApiOperation("修改最终实现")
    @PutMapping("updateTeacher")
    public Result updataTeacher(@RequestBody Teacher teacher){
        boolean b = teacherService.updateById(teacher);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //7、批量删除
    @ApiOperation("批量删除")
    @DeleteMapping("deleteBatchids")
    public Result deleteBathchids(@RequestBody List<Long> ids){
        boolean b = teacherService.removeByIds(ids);
        if (b){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }


    }





}

