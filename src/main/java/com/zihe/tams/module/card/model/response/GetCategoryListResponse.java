package com.zihe.tams.module.card.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class GetCategoryListResponse {
    @ApiModelProperty(value = "id", position = 3)
    private Long id;

    @ApiModelProperty(value = "名称", position = 3)
    private String name;

    @ApiModelProperty(value = "时长", position = 3)
    private Integer validityPeriod;

    @ApiModelProperty(value = "时间段 1: 工作日 2: 周末", position = 4)
    private Integer period;

    @ApiModelProperty(value = "可用次数", position = 5)
    private Integer frequency;

    @ApiModelProperty(value = "是否是vip团课 0否 1是", position = 6)
    private Integer isGroupClass;

    @ApiModelProperty(value = "是否是常规私教 0否 1是", position = 7)
    private Integer isPrivateEducation;

    @ApiModelProperty(value = "是否是普拉提器械私教 0否 1是", position = 8)
    private Integer isPrivatePilates;

    @ApiModelProperty(value = "是否是充值卡 0否 1是", position = 9)
    private Integer isRechargeableCard;

    @ApiModelProperty(value = "优惠额度", position = 10)
    private BigDecimal discountRate;

    @ApiModelProperty(value = "是否是大礼包充值卡  0否 1是", position = 11)
    private Integer isSpecialRechargeableCard;

    @ApiModelProperty(value = "是否是本馆专用卡  0否 1是", position = 12)
    private Integer isSpecialSelf;
}
