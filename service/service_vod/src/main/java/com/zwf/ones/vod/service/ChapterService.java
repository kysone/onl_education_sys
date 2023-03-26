package com.zwf.ones.vod.service;

import com.zwf.ones.model.vod.Chapter;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwf.ones.vo.vod.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-03-22
 */
public interface ChapterService extends IService<Chapter> {

    List<ChapterVo> getTreeList(Long courseId);

    void removeChapterByCourseId(Long id);
}
