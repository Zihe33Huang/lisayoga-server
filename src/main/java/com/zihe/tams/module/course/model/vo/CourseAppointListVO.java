package com.zihe.tams.module.course.model.vo;

import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/3/16 3:04 下午
 */
@Data
public class CourseAppointListVO {

    Long customerId;

    Long cardId;

    String name;

    String cardName;

    String appointTime;

    String cancelTime;

    Integer state;

    Integer attendance;

    String phoneNum;

    /**
     * 该学生上该老师课的次数
     */
    Integer attendNum;

    Integer age;

    /**
     * 0 没练过瑜伽  1 练过
     */
    Integer hasPracticed;

    /**
     * 0 没受过伤   1 受过伤
     */
    Integer hasHurt;

    /**
     * 目的
     */
    String aim;

    /**
     * 0 正式会员  1 体验会员  2 体验转正
     */
    Integer customerType;
}
