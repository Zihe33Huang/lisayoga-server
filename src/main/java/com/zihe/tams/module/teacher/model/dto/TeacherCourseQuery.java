package com.zihe.tams.module.teacher.model.dto;

import com.zihe.tams.common.model.BasePageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/4/7 9:13 下午
 */
@Data
public class TeacherCourseQuery extends BasePageQuery {

    private Long teacherId;

    @ApiModelProperty(value = "开始日期")
    private String startDate;

    @ApiModelProperty(value = "结束日期")
    private String endDate;
}
