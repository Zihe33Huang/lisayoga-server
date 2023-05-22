package com.zihe.tams.module.course.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author huangzihe
 * @date 2022/3/20 1:58 上午
 */
@Data
public class CustomerCourseListVO {
    private Integer cardType;

    /**
     * 1、 已学  2、已约
     */
    private Integer studyState;

    private Integer appointState;

    /**
     * 上课日期
     */
    private LocalDate date;

    /**
     * 上课时间
     */
    private LocalTime attendTime;

    private String attendTimeStr;

    /**
     * 下课时间
     */
    private LocalTime finishTime;

    private String finishTimeStr;

    private String courseName;

    private String teacherName;

    private String akaName;

    private Integer attendance;

    private String cardName;

    Long cardId;

    Long scheduleId;
}
