package com.zihe.tams.module.course.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CourseAppointStateEnum {

    Appointed(1, "已约"), WAIT(2, "候补"), Cancel(3, "取消");

    private Integer code;

    private String desc;
}
