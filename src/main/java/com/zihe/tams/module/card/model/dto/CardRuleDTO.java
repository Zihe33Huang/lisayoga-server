package com.zihe.tams.module.card.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 卡规则 DTO
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "卡规则")
public class CardRuleDTO {


	@ApiModelProperty(value = "id", position = 1)
	private Long id;

	@ApiModelProperty(value = "名称", position = 2)
	private String name;

	@ApiModelProperty(value = "时长", position = 3)
	private Integer validityPeriod;

	@ApiModelProperty(value = "时间段 1: 工作日 2: 周末", position = 4)
	private Integer period;

	@ApiModelProperty(value = "1：时长卡 2：次卡 3：充值卡", position = 9)
	private Integer type;

//	@ApiModelProperty(value = "优惠额度", position = 10)
//	private BigDecimal discountRate;


}
