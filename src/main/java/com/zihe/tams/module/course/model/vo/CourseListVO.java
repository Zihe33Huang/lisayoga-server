package com.zihe.tams.module.course.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(value = "课程列表返回结果")
@Data
public class CourseListVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "停启用状态")
    private Integer enableState;

    @ApiModelProperty(value = "课程时长，单位分钟")
    private Integer duration;

    @ApiModelProperty(value = "背景颜色")
    private String backgroundColor;

    private String description;

    /**
     * 可用卡种类
     */
    private String cardType;


    private List<Integer> cardTypeList;

    /**
     * 最多可容纳人数
     */
    private Integer maximum;

    /**
     * 最少开课人数
     */
    private Integer minimum;

    /**
     * 课程价格
     */
    private BigDecimal price;


}
