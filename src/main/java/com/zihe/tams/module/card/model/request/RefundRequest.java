package com.zihe.tams.module.card.model.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RefundRequest {

    private Long cardId;

    private Long customerId;

    private Integer refundMethod;

    private String remark;

    // 手续费比例
    private BigDecimal chargeRate = new BigDecimal(30);

    private Integer payMethod;

}
