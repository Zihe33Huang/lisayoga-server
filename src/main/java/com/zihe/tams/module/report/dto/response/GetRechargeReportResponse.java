package com.zihe.tams.module.report.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GetRechargeReportResponse {

    @ApiModelProperty(value = "客户id", position = 2)
    private String customerName;

    private String type;

    @ApiModelProperty(value = "卡名称", position = 3)
    private String cardName;

    @ApiModelProperty(value = "支付金额", position = 4)
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "支付方式", position = 5)
    private String payMethod;

    @ApiModelProperty(value = "实际费用", position = 7)
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "充值次数")
    private Integer cardTimes;

    @ApiModelProperty(value = "充值天数")
    private Integer validityPeriod;


    @ApiModelProperty(value = "创建时间", position = 13)
    private LocalDateTime createTime;


}
