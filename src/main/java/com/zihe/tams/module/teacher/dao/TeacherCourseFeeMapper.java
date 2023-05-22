package com.zihe.tams.module.teacher.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zihe.tams.module.teacher.model.data.TeacherCourseFeeDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangzihe
 * @date 2022/4/9 7:51 下午
 */
public interface TeacherCourseFeeMapper extends BaseMapper<TeacherCourseFeeDO>  {
    public List<TeacherCourseFeeDO> queryTeacherConfig(@Param("teacherId") Long teacherId, @Param("page")Integer page, @Param("size") Integer size, @Param("name") String name);

    public List<TeacherCourseFeeDO> queryByTeacherId(@Param("teacherId") Long teacherId);

    public int getTotal(@Param("teacherId") Long teacherId , @Param("name") String name);
}
