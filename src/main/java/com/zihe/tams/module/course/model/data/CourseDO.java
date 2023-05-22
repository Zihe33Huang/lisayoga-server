package com.zihe.tams.module.course.model.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_course")
public class CourseDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

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
    private String cardType;

    /**
     * 最多可容纳人数
     */
    private Integer maximum;

    /**
     * 最少开课人数
     */
    private Integer minimum;

    /**
     * 课程时长，单位分钟
     */
    private Integer duration;

    /**
     * 背景颜色
     */
    private String backgroundColor;

    /**
     * 课程价格
     */
    private BigDecimal price;
}
