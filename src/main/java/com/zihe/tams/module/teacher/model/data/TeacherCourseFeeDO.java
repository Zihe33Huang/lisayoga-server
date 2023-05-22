package com.zihe.tams.module.teacher.model.data;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author huangzihe
 * @date 2022/4/9 7:50 下午
 */
@Data
@TableName("t_teacher_course_fee")
public class TeacherCourseFeeDO {
    private Long id;

    private Long teacherId;

    private Long courseId;

    private BigDecimal courseFee;
}
