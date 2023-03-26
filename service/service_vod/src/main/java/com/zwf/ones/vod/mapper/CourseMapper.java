package com.zwf.ones.vod.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zwf.ones.model.vod.Course;
import com.zwf.ones.vo.vod.CoursePublishVo;
import com.zwf.ones.vo.vod.CourseVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2023-03-22
 */
public interface CourseMapper extends BaseMapper<Course> {

    CoursePublishVo selectCoursePublishVoById(Long id);





    CourseVo selectCourseVoById(Long courseId);
}
