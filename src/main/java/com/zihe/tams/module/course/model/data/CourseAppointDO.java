package com.zihe.tams.module.course.model.data;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author huangzihe
 * @date 2022/3/15 7:35 下午
 */
@Data
@TableName("t_course_appoint")
public class CourseAppointDO {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private Long scheduleId;

    private Long customerId;

    private Long cardId;

    private Date appointTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date cancelTime;

    private Integer state;

    private Integer attendance;

    private Character isDeleted;


}
