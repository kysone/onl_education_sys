package com.zwf.ones.vod.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zwf.ones.model.vod.VideoVisitor;
import com.zwf.ones.vo.vod.VideoVisitorCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 视频来访者记录表 Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2023-03-23
 */
public interface VideoVisitorMapper extends BaseMapper<VideoVisitor> {


    List<VideoVisitorCountVo> findCount(@Param("courseId") Long courseId,
                                        @Param("startDate") String startDate,
                                        @Param("endDate") String endDate);

}
