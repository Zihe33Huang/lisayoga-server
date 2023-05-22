package com.zihe.tams.module.course.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourseAppointAttendanceEnum {

    UNSIGNED(0, "未签到"), SIGN(1, "签到");

    private Integer code;

    private String desc;
}
