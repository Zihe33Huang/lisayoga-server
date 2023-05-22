package com.zihe.tams.module.teacher.model.dto;

import com.zihe.tams.module.coursescheduling.model.vo.CourseSchedulingListVO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author huangzihe
 * @date 2022/4/10 3:22 上午
 */
@Data
public class SchedulingCourseFeeDTO extends CourseSchedulingListVO {
    private BigDecimal courseFee;

    private String time;
}
