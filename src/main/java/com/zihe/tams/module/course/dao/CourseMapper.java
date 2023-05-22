package com.zihe.tams.module.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zihe.tams.module.course.model.data.CourseDO;

import java.util.List;

public interface CourseMapper extends BaseMapper<CourseDO> {
    List<Long> getAllCourse();
}
