package com.zihe.tams.module.card.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员卡退款记录 DTO
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "会员卡退款记录")
public class RefundDTO {


	@ApiModelProperty(value = "id", position = 1)
	private Long id;

	@ApiModelProperty(value = "客户id", position = 2)
	private Long customerId;

	@ApiModelProperty(value = "卡号", position = 3)
	private String cardNo;

	@ApiModelProperty(value = "卡类型", position = 4)
	private String categoryName;

	@ApiModelProperty(value = "总价", position = 5)
	private BigDecimal totalPrice;

	@ApiModelProperty(value = "总次数", position = 6)
	private Integer totalTimes;

	@ApiModelProperty(value = "平均价格", position = 7)
	private BigDecimal averageprice;

	@ApiModelProperty(value = "实际费用", position = 8)
	private BigDecimal actualAmount;

	@ApiModelProperty(value = "退款次数", position = 9)
	private Integer refundTimes;

	@ApiModelProperty(value = "应退款次数", position = 10)
	private BigDecimal refundAmount;

	@ApiModelProperty(value = "手续费比例", position = 11)
	private BigDecimal chargeRate;

	@ApiModelProperty(value = "手续费", position = 12)
	private BigDecimal refundCharge;

	@ApiModelProperty(value = "实际退款", position = 13)
	private BigDecimal actualRefund;

	@ApiModelProperty(value = "退款方式", position = 14)
	private String refundMethod;

	@ApiModelProperty(value = "备注", position = 15)
	private String remark;

	@ApiModelProperty(value = "创建人姓名/昵称", position = 16)
	private String creator;

	@ApiModelProperty(value = "创建人编码", position = 17)
	private String creatorCode;

	@ApiModelProperty(value = "更新人姓名/昵称", position = 18)
	private String updater;

	@ApiModelProperty(value = "更新人编码", position = 19)
	private String updaterCode;

	@ApiModelProperty(value = "创建时间", position = 20)
	private LocalDateTime createTime;

	@ApiModelProperty(value = "更新时间", position = 21)
	private LocalDateTime updateTime;


}
