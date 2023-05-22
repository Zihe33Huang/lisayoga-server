package com.zihe.tams.module.teacher.model.dto;

import com.zihe.tams.module.teacher.model.data.TeacherCourseFeeDO;
import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/4/9 7:51 下午
 */
@Data
public class TeacherCourseFeeDTO extends TeacherCourseFeeDO {

    private String CourseName;
}
