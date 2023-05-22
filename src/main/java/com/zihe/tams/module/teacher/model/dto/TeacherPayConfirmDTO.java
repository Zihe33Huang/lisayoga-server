package com.zihe.tams.module.teacher.model.dto;

import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/4/9 2:51 下午
 */
@Data
public class TeacherPayConfirmDTO extends TeacherPayDTO {

    private String month;

    private Long teacherId;

    private Integer type;
}
