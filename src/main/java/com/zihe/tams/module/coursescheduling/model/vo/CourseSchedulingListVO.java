package com.zihe.tams.module.coursescheduling.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ApiModel(value = "排课列表返回结果")
@Data
public class CourseSchedulingListVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "教室id")
    private Long classroomId;

    @ApiModelProperty(value = "教室名称")
    private String classroomName;

    @ApiModelProperty(value = "课程id")
    private Long courseId;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "课程时长，单位分钟")
    private Integer duration;

    @ApiModelProperty(value = "课程背景颜色")
    private String backgroundColor;

    @ApiModelProperty(value = "老师id")
    private Long teacherId;

    @ApiModelProperty(value = "老师姓名")
    private String teacherName;

    private String akaName;

    private String pictureUrl;

    @ApiModelProperty(value = "日期")
    private LocalDate date;

    @ApiModelProperty(value = "上课时间")
    private LocalTime attendTime;

    @ApiModelProperty(value = "下课时间")
    private LocalTime finishTime;

    private String cardType;

    private List<String> cardTypeList;

    private Integer maximum;

    private BigDecimal price;

    /**
     * 确认签到人数
     */
    private Integer signInNum;

    /**
     * 实际约课人数  已约 + 候补
     */
    private Integer appointNum;

    private String description;

    private String introduction;

    private String qualification;

}
