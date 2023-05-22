package com.zihe.tams.module.card.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;


/**
 * 卡规则 PageParam
 * 继承 CardRuleParam 的属性，这里不用添加任何属性，保持分页和非分页查询参数一致
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@Data
@ToString(callSuper = true)
@ApiModel(description = "卡规则 PageParam")
public class CardRulePageParam  {

}