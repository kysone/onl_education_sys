package com.zwf.ones.vod.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zwf.ones.model.vod.Course;
import com.zwf.ones.model.vod.CourseDescription;
import com.zwf.ones.model.vod.Subject;
import com.zwf.ones.model.vod.Teacher;
import com.zwf.ones.vo.vod.*;
import com.zwf.ones.vod.mapper.CourseMapper;
import com.zwf.ones.vod.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-03-22
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Resource
    private CourseQueryVo courseQueryVo;

    @Resource
    private TeacherService teacherService;

    @Resource
    private SubjectService subjectService;

    @Resource
    private CourseDescriptionService descriptionService;

    @Resource
    private VideoService videoService;

    @Resource
    private ChapterService chapterService;


    /**
     * 点播课程列表
     * */
    @Override
    public Map<String, Object> findPageCourse(Page<Course> page1, CourseQueryVo courseQueryVo) {

        //获取条件
        Long subjectId = courseQueryVo.getSubjectId();
        Long subjectParentId = courseQueryVo.getSubjectParentId();
        Long teacherId = courseQueryVo.getTeacherId();
        String title = courseQueryVo.getTitle();

        QueryWrapper<Course> wrapper=new QueryWrapper<>();

        if(!StringUtils.isEmpty(title)){
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id",teacherId);
        }

        Page<Course> coursePage = baseMapper.selectPage(page1, wrapper);
        long totalCount = coursePage.getTotal();
        long totalPage = coursePage.getPages();
        List<Course> records = coursePage.getRecords();
        System.out.println(records);

        records.stream().forEach(item->{
            this.getNameByID(item);
        });


        //封装数据
        Map<String,Object> map=new HashMap<>();
        map.put("totalCount",totalCount);
        map.put("totalPage",totalPage);
        map.put("records",records);
        return map;
    }

    //添加课程信息
    @Override
    public Long saveCourseInfo(CourseFormVo courseFormVo) {
        //添加课程的基本信息
        Course course=new Course();
        BeanUtils.copyProperties(courseFormVo,course);
        baseMapper.insert(course);

        //添加课程描述
        CourseDescription courseDescription=new CourseDescription();
        courseDescription.setDescription(courseFormVo.getDescription());

        courseDescription.setId(course.getId());
        descriptionService.save(courseDescription);

        return course.getId();
    }

    @Override
    public CourseFormVo geCourseInfoById(Long id) {
        Course course = baseMapper.selectById(id);
        if (course==null){
            return null;
        }
        CourseDescription courseDescription=descriptionService.getById(id);
        CourseFormVo courseFormVo=new CourseFormVo();
        BeanUtils.copyProperties(course,courseFormVo);
        if(courseDescription!=null){
            courseFormVo.setDescription(courseDescription.getDescription());
        }

        return courseFormVo;

    }

    @Override
    public void updateCourseId(CourseFormVo courseFormVo) {
        Course course =new Course();
        BeanUtils.copyProperties(courseFormVo,course);
        baseMapper.updateById(course);

        //修改课程描述信息
        CourseDescription description = new CourseDescription();

        description.setId(course.getId());
        description.setDescription(courseFormVo.getDescription());
        descriptionService.updateById(description);
    }

    @Override
    public CoursePublishVo getCoursePublishVo(Long id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    @Override
    public void publishCourse(Long id) {
        Course course = baseMapper.selectById(id);
        course.setStatus(1);//已发布
        course.setPublishTime(new Date());
        baseMapper.updateById(course);
  }

    @Override
    public void removeCourseId(Long id) {
        videoService.removeVideoByCourseId(id);

        //根据课程id 删除章节
        chapterService.removeChapterByCourseId(id);

        //根据课程id 删除课程描述
        descriptionService.removeById(id);

        baseMapper.deleteById(id);

    }

    //根据课程分类查询课程列表（分页）用于微信公众号里的查询
    @Override
    public Map<String, Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        //获取条件值
        String title = courseQueryVo.getTitle();//课程名称
        Long subjectId = courseQueryVo.getSubjectId();//二级分类
        Long subjectParentId = courseQueryVo.getSubjectParentId();//一级分类
        Long teacherId = courseQueryVo.getTeacherId();//讲师
        //判断条件值是否为空，封装条件（其实就传了一个subjectid大可不必判空）
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(title)) {
            wrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(subjectId)) {
            wrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(subjectParentId)) {
            wrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(teacherId)) {
            wrapper.eq("teacher_id",teacherId);
        }
        //调用方法进行条件分页查询
        Page<Course> pages = baseMapper.selectPage(pageParam, wrapper);
        //获取分页数据
        long totalCount = pages.getTotal();//总记录数
        long totalPage = pages.getPages();//总页数
        long currentPage = pages.getCurrent();//当前页
        long size = pages.getSize();//每页记录数
        //每页数据集合
        List<Course> records = pages.getRecords();

        //封装其他数据（获取讲师名称和课程分类名称）
        records.stream().forEach(item -> {
            this.getTeacherOrSubjectName(item);//调用下边方法获取讲师和分类名称
        });
        Map<String,Object> map = new HashMap<>();
        map.put("totalCount",totalCount);
        map.put("totalPage",totalPage);
        map.put("records",records);
        return map;
    }
    //获取讲师和分类名称
    private Course getTeacherOrSubjectName(Course course) {
        //获取讲师名称
        Long teacherId=course.getTeacherId();
        //根据id查询讲师信息
        Teacher teacher = teacherService.getById(teacherId);
        if(teacher != null) {
            course.getParam().put("teacherName",teacher.getName());
        }
        //获取课程一级分类名称
        Long subjectParentId=course.getSubjectParentId();
        //根据一级分类id查询一级分类信息
        Subject subjectOne = subjectService.getById(subjectParentId);
        if(subjectOne != null) {
            course.getParam().put("subjectParentTitle",subjectOne.getTitle());
        }
        //根据二级分类id查询二级分类信息
        Long subjectId=course.getSubjectId();
        Subject subjectTwo = subjectService.getById(subjectId);
        if(subjectTwo != null) {
            course.getParam().put("subjectTitle",subjectTwo.getTitle());
        }
        return course;
    }

    //根据课程ID查询课程详情信息
    @Override
    public Map<String, Object> getInfoById(Long courseId) {
        //更新浏览量  view_count表字段流量+1
        Course course = baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount() + 1);
        baseMapper.updateById(course);
        //根据课程id查询课程详情数据（自己写的方法在自己的实现层就调用basemapper）
        CourseVo courseVo = baseMapper.selectCourseVoById(courseId);
        //课程章节小节数据（之前写的方法）大纲列表（章节和小节列表也叫课时）树形结构显示
        List<ChapterVo> chapterVoList = chapterService.getTreeList(courseId);
        //课程描述信息
        CourseDescription courseDescription = descriptionService.getById(courseId);
        //课程所属信息讲师信息
        Teacher teacher = teacherService.getById(course.getTeacherId());

        //TODO后续完善
        Boolean isBuy = false;
        //封装map集合，返回
        Map<String, Object> map = new HashMap<>();
        map.put("courseVo", courseVo);
        map.put("chapterVoList", chapterVoList);
        map.put("description", null != courseDescription ?
                courseDescription.getDescription() : "");
        map.put("teacher", teacher);
        map.put("isBuy", isBuy);//是否购买
        return map;
    }


    //获取这些id 对应的名称，进行封装，最终显示
    private Course getNameByID(Course course) {
        //根据讲师的id获取讲师的名称
        Teacher teacher = teacherService.getById(course.getTeacherId());
        if (teacher!=null){
            String name = teacher.getName();
            course.getParam().put("teacherName",name);
        }
        //根据课程分类的id获取课程分类的名称
        Subject subjectOne = subjectService.getById(course.getSubjectParentId());
        if (subjectOne!=null){
            course.getParam().put("subjectParentTitle",subjectOne.getTitle());
        }
        Subject subjectTwo = subjectService.getById(course.getSubjectId());
        if(subjectTwo != null) {
            course.getParam().put("subjectTitle",subjectTwo.getTitle());
        }

        return course;
    }

}
