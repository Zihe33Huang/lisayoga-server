package com.zihe.tams.module.card.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateCardRequest {

    @ApiModelProperty(value = "顾客id")
    @NotBlank(message = "顾客id不可为空")
    private String customerId;

    @ApiModelProperty(value = "日期")
    private String createTime;

    @ApiModelProperty(value = "卡类别")
    @NotBlank(message = "卡类别不可为空")
    private String cardRuleId;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "次数")
    private Integer cardTimes;

    @ApiModelProperty(value = "起始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "有效期")
    private Integer validityPeriod;

    @ApiModelProperty(value = "支付方式")
    private String payMethod;

    @ApiModelProperty(value = "渠道费用")
    private BigDecimal channelFee;

    @ApiModelProperty(value = "实际费用")
    private BigDecimal actualAmount;

    private Long teacherId;

    private Integer isFree;
}
