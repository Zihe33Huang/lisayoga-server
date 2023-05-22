package com.zihe.tams.module.teacher.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author huangzihe
 * @date 2022/4/9 4:55 上午
 */
@Data
public class TeacherPayDTO {

    private Long teacherId;
    private String teacherName;

    private Integer count;

    private BigDecimal courseFee;

    private BigDecimal basicSalary;

    private BigDecimal allowance;

    private BigDecimal  totalSalary;

    List<SchedulingCourseFeeDTO> feeDTO;

    /**
     * 提成工资
     */
    BigDecimal royalty;

    BigDecimal royaltyRate;

    /**
     * 结算状态： 0 未结算  1 已结算
     */
    private Integer status;

    String month;

    Integer type;

    Integer payType;
}
