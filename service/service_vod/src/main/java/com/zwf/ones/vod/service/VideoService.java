package com.zwf.ones.vod.service;

import com.zwf.ones.model.vod.Video;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-03-22
 */
public interface VideoService extends IService<Video> {

    void removeVideoByCourseId(Long id);
}
