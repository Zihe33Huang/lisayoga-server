package com.zihe.tams.module.customer.model.dto;

import com.zihe.tams.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/3/10 8:41 下午
 */

@ApiModel(value = "会员分页查询参数")
@Data
public class CustomerPageQuery extends BasePageQuery {
    @ApiModelProperty(value = "停启用状态")
    private Integer enableState;
}
