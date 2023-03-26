package com.zwf.ones.vod.service;

import com.zwf.ones.model.vod.Subject;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-03-21
 */
public interface SubjectService extends IService<Subject> {

    List<Subject> selectList(Long id);


    void exportData(HttpServletResponse response);


    void importDictData(MultipartFile file);
}
