package com.zihe.tams.module.course.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(value = "课程新增参数")
@Data
public class CourseSaveDTO {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "课程时长，单位分钟")
    private Integer duration;

    @ApiModelProperty(value = "背景颜色")
    private String backgroundColor;


    /**
     * 课程描述
     */
    private String description;

    /**
     * 停启用状态
     */
    private Integer enableState;

    /**
     * 可用卡种类
     */
    private List<Integer> cardTypeList;

    private String cardType;

    /**
     * 最多可容纳人数
     */
    private Integer maximum;

    /**
     * 最少开课人数
     */
    private Integer minimum;


    private BigDecimal price;

}
