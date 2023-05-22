package com.zihe.tams.module.teacher.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zihe.tams.module.teacher.model.data.TeacherDO;

import java.util.List;

public interface TeacherMapper extends BaseMapper<TeacherDO> {
    List<Long> getAllTeacher();
}
