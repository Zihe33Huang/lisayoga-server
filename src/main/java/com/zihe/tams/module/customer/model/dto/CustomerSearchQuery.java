package com.zihe.tams.module.customer.model.dto;

import com.zihe.tams.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/3/26 1:29 上午
 */
@ApiModel(value = "会员分页模糊查询参数")
@Data
public class CustomerSearchQuery extends BasePageQuery {
    String name;

    Integer enableState;
}