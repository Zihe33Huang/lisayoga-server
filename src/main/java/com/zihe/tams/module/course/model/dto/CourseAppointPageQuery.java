package com.zihe.tams.module.course.model.dto;

import com.zihe.tams.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/3/16 3:06 下午
 */

@ApiModel(value = "课程预约分页查询参数")
@Data
public class CourseAppointPageQuery extends BasePageQuery {

    @ApiModelProperty(value = "课程安排Id")
    private Long scheduleId;

    @ApiModelProperty(value = "会员id")
    private Long customerId;

}
