package com.zihe.tams.module.teacher.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeacherTypeEnum {

    FULLTIME(0, "全职"), PARTTIME(1, "兼职");

    private Integer code;
    private String decs;
}
