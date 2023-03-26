package com.zwf.ones.vod.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zwf.ones.model.vod.Course;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwf.ones.vo.vod.CourseFormVo;
import com.zwf.ones.vo.vod.CoursePublishVo;
import com.zwf.ones.vo.vod.CourseQueryVo;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-03-22
 */

public interface CourseService extends IService<Course> {

    Map<String, Object> findPageCourse(Page<Course> page1, CourseQueryVo courseQueryVo);

    Long saveCourseInfo(CourseFormVo courseFormVo);

    CourseFormVo geCourseInfoById(Long id);

    void updateCourseId(CourseFormVo courseFormVo);

    CoursePublishVo getCoursePublishVo(Long id);

    void publishCourse(Long id);

    void removeCourseId(Long id);

    Map<String, Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo);

    Map<String, Object> getInfoById(Long courseId);
}
