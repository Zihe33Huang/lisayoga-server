package com.zihe.tams.module.card.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员卡充值记录 DTO
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "会员卡充值记录")
public class RechargeDTO {


	@ApiModelProperty(value = "id", position = 1)
	private Long id;

	@ApiModelProperty(value = "客户id", position = 2)
	private Long customerId;

	@ApiModelProperty(value = "卡号", position = 3)
	private String cardNo;

	@ApiModelProperty(value = "支付金额", position = 4)
	private BigDecimal paymentAmount;

	@ApiModelProperty(value = "支付方式", position = 5)
	private String payMethod;

	@ApiModelProperty(value = "渠道费用", position = 6)
	private BigDecimal channelFee;

	@ApiModelProperty(value = "实际费用", position = 7)
	private BigDecimal actualAmount;

	@ApiModelProperty(value = "充值次数")
	private Integer cardTimes;

	@ApiModelProperty(value = "充值天数")
	private Integer validityPeriod;

	@ApiModelProperty(value = "备注", position = 8)
	private String remark;

	@ApiModelProperty(value = "创建人姓名/昵称", position = 9)
	private String creator;

	@ApiModelProperty(value = "创建人编码", position = 10)
	private String creatorCode;

	@ApiModelProperty(value = "更新人姓名/昵称", position = 11)
	private String updater;

	@ApiModelProperty(value = "更新人编码", position = 12)
	private String updaterCode;

	@ApiModelProperty(value = "创建时间", position = 13)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "更新时间", position = 14)
	private LocalDateTime updateTime;


}
