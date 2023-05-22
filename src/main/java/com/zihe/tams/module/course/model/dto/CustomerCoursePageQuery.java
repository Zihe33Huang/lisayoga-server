package com.zihe.tams.module.course.model.dto;

import com.zihe.tams.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/3/20 2:03 上午
 */
@ApiModel(value = "会员上课查询参数")
@Data
public class CustomerCoursePageQuery extends BasePageQuery {

    private Long customerId;

    private Long scheduleId;

    @ApiModelProperty(value = "停启用状态")
    private Integer enableState;
}
