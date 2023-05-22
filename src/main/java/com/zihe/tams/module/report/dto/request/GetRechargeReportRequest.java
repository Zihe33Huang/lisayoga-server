package com.zihe.tams.module.report.dto.request;

import com.zihe.tams.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class GetRechargeReportRequest extends BasePageQuery {
    private String name;
}
