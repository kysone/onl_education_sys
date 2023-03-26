package com.zwf.ones.vod.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zwf.ones.model.vod.Chapter;
import com.zwf.ones.model.vod.Video;
import com.zwf.ones.vo.vod.ChapterVo;
import com.zwf.ones.vo.vod.VideoVo;
import com.zwf.ones.vod.mapper.ChapterMapper;
import com.zwf.ones.vod.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwf.ones.vod.service.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-03-22
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoService videoService;

    @Override
    public List<ChapterVo> getTreeList(Long courseId) {
        List<ChapterVo> finalChapterList = new ArrayList<>();

        QueryWrapper<Chapter> wrapperChapter = new QueryWrapper<>();
        List<Chapter> chapterList = baseMapper.selectList(wrapperChapter);

        LambdaQueryWrapper<Video> wrapperVideo = new LambdaQueryWrapper<>();
        wrapperVideo.eq(Video::getCourseId, courseId);
        List<Video> videoList = videoService.list(wrapperVideo);



        for (int i=0;i<chapterList.size (); i++){
            //得到课程里面的每个章节
            Chapter chapter = chapterList.get(i);
            //chapter-->ChapterVo
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);
            //得到的每个章节对象放到finalChapterList集合
            finalChapterList.add(chapterVo);

            //封装章节里面小节
            //创建list集合用户封装章节所有小节
            List<VideoVo> videoVoList = new ArrayList<>();
            //遍历小节
            for (Video video : videoList) {
                //判断小节是哪个章节下面的小节
                //章节的id和小节里面的chapter_id
                if (chapter.getId().equals(video.getChapterId())) {
                    VideoVo videoVo = new VideoVo();
                    //放到videoVoList
                    //video-->videoVo
                    BeanUtils.copyProperties(video, videoVo);
                    videoVoList.add(videoVo);
                }
            }
            //把章节里面的所有小节集合放到每个章节里面
            chapterVo.setChildren(videoVoList);
        }
        return finalChapterList;

    }

    @Override
    public void removeChapterByCourseId(Long id) {
        QueryWrapper<Chapter> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",id);
        baseMapper.delete(wrapper);
    }
}
