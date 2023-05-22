package com.zihe.tams.module.teacher.model.dto;

import lombok.Data;

import java.time.LocalTime;

/**
 * @author huangzihe
 * @date 2022/4/7 9:12 下午
 */
@Data
public class TeacherCourseDTO {

    private String date;

    private String time;

    private String courseName;

    private String studentName;

    /**
     * 上课时间
     */
    private LocalTime attendTime;

    /**
     * 下课时间
     */
    private LocalTime finishTime;
}
