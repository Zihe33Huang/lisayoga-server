package com.zihe.tams.module.card.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class RechargeRequest {
    @ApiModelProperty(value = "顾客id")
    @NotBlank(message = "顾客id不可为空")
    private String customerId;

    @ApiModelProperty(value = "卡类别")
    @NotBlank(message = "卡类别不可为空")
    private String cardId;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "支付方式")
    private String payMethod;

    @ApiModelProperty(value = "渠道费用")
    private BigDecimal channelFee;

    @ApiModelProperty(value = "实际费用")
    private BigDecimal actualAmount;
}
