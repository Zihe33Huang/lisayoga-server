package com.zihe.tams.module.card.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author huangzihe
 * @date 2022/3/30 3:43 下午
 */
@Data
public class CardListVO {

    String cardId;

    String customerId;

    String customerName;

    String cardName;

    @ApiModelProperty(value = "剩余可用次数")
    private Integer availableCount;

    @ApiModelProperty(value = "剩余可用金额")
    private BigDecimal availableAmount;

    @ApiModelProperty(value = "剩余可用天数")
    private Integer availablePeriod;

    @ApiModelProperty(value = "有效期始")
    private String openCardDay;

    @ApiModelProperty(value = "有效期止")
    private String endCardDay;

    Integer type;
}
