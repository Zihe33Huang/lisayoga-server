package com.zihe.tams.module.card.model.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class GetPersonCardInfoResponse {

    @ApiModelProperty(value = "卡ID")
    private String id;

    @ApiModelProperty(value = "卡类别名称")
    private String cardName;

    @ApiModelProperty(value = "类别")
    private Integer type;

    @ApiModelProperty(value = "总付款次数")
    private Integer cardTimes;

    @ApiModelProperty(value = "已学次数")
    private Integer useCount;

    @ApiModelProperty(value = "剩余可用次数")
    private Integer availableCount;

    @ApiModelProperty(value = "总付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "已学金额")
    private BigDecimal useAmount;

    @ApiModelProperty(value = "剩余可用金额")
    private BigDecimal availableAmount;

    @ApiModelProperty(value = "总天数")
    private Integer validityPeriod;

    @ApiModelProperty(value = "已使用天数")
    private Integer usePeriod;

    @ApiModelProperty(value = "剩余可用天数")
    private Integer availablePeriod;

    @ApiModelProperty(value = "支付方式")
    private String payMethod;

    @ApiModelProperty(value = "充值次数")
    private Integer payTimes;

    @ApiModelProperty(value = "渠道费用")
    private BigDecimal channelFee;

    @ApiModelProperty(value = "收入")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "日期")
    private String createTIme;

    @ApiModelProperty(value = "有效期始")
    private String openCardDay;

    @ApiModelProperty(value = "有效期止")
    private String endCardDay;


    private Integer enableState;

    private Integer isFree;

    private String teacherName;

    private Integer cancelTimes;


}
