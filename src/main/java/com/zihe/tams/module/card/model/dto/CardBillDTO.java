package com.zihe.tams.module.card.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员卡流水 DTO
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "会员卡流水")
public class CardBillDTO {


	@ApiModelProperty(value = "id", position = 1)
	private Long id;

	@ApiModelProperty(value = "客户id", position = 2)
	private Long customerId;

	@ApiModelProperty(value = "来源编码", position = 3)
	private Long sourceId;

	@ApiModelProperty(value = "卡号", position = 4)
	private String cardNo;

	@ApiModelProperty(value = "类型 1:充值 2：退款", position = 5)
	private Integer type;

	@ApiModelProperty(value = "余额", position = 6)
	private BigDecimal preBalance;

	@ApiModelProperty(value = "消费金额", position = 7)
	private BigDecimal consumptionAmount;

	@ApiModelProperty(value = "现有余额 余额减去消费金额", position = 8)
	private BigDecimal availableBalance;


	@ApiModelProperty(value = "可用次数")
	private Integer preCount;

	@ApiModelProperty(value = "消费次数")
	private Integer consumptionCount;

	@ApiModelProperty(value = "现有次数 可用次数减去消费次数 ")
	private Integer availableCount;

	@ApiModelProperty(value = "备注", position = 9)
	private String remark;

	@ApiModelProperty(value = "创建人姓名/昵称", position = 10)
	private String creator;

	@ApiModelProperty(value = "创建人编码", position = 11)
	private String creatorCode;

	@ApiModelProperty(value = "更新人姓名/昵称", position = 12)
	private String updater;

	@ApiModelProperty(value = "更新人编码", position = 13)
	private String updaterCode;

	@ApiModelProperty(value = "创建时间", position = 14)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "更新时间", position = 15)
	private LocalDateTime updateTime;

}
