package com.zihe.tams.module.course.model.dto;

import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/3/15 7:28 下午
 */
@Data
public class CourseAppointDTO {

    Long scheduleId;

    Long customerId;

    Long cardId;

    /**
     * 区分b端还是c端  0 b端 1c端
     */
    Integer endType;
}
