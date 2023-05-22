package com.zihe.tams.module.card.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员卡 DTO
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "会员卡")
public class CardDTO {


	@ApiModelProperty(value = "id", position = 1)
	private Long id;

	@ApiModelProperty(value = "客户id", position = 2)
	private Long customerId;

	@ApiModelProperty(value = "卡规则id", position = 3)
	private Long cardRuleId;

	@ApiModelProperty(value = "卡号", position = 4)
	private String cardNo;

	@ApiModelProperty(value = "备注", position = 5)
	private String remark;


	@ApiModelProperty(value = "余额", position = 6)
	private BigDecimal balance;

	@ApiModelProperty(value = "开始时间", position = 7)
	private LocalDateTime startTime;

	@ApiModelProperty(value = "有效期", position = 8)
	private Integer validityPeriod;


	@ApiModelProperty(value = "可用次数", position = 8)
	private Integer validityCount;

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

	@ApiModelProperty(value = "状态")
	private Integer status;

	private Integer enableState;

	private Long teacherId;

	private Integer cancelTimes;

}
