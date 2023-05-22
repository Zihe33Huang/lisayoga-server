package com.zihe.tams.module.teacher.model.data;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author huangzihe
 * @date 2022/4/9 2:36 下午
 */
@Data
@TableName("t_teacher_salary")
public class TeacherSalaryDO {

    Long id;

    Long teacherId;

    BigDecimal basicSalary;

    Integer count;

    String month;

    BigDecimal allowance;


    BigDecimal  totalSalary;


    /**
     * 提成工资
     */
    BigDecimal royalty;

    BigDecimal royaltyRate;

    BigDecimal courseFee;

    Integer type;
}
