package com.zihe.tams.module.teacher.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author huangzihe
 * @date 2022/4/9 4:58 上午
 */
@Data
public class TeacherPayQuery {

    private String month;

    private Long teacherId;

    BigDecimal courseFee;

    String type;

    private String startDate;

    private String endDate;
}
