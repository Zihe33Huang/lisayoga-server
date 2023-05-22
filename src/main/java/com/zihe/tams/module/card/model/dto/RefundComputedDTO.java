package com.zihe.tams.module.card.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author huangzihe
 * @date 2022/4/13 5:44 下午
 */
@Data
public class RefundComputedDTO {

    // 退卡次数
    BigDecimal refundCount;

    // 平均价格 = 充值总价格 / 充值总次数
    BigDecimal averagePrice;

    // 退卡金额 == 剩余价格 = 平均价格 * 剩余次数/天数
    BigDecimal refundAmount;

    // 充值总次数 || 天数 || 总金额
    BigDecimal totalCount;

    // 退卡金额 == 剩余价格 = 平均价格 * 剩余次数/天数
    BigDecimal actualRefundAmount;

    BigDecimal serviceCharge;

    BigDecimal rate;

    // 总充值金额
    BigDecimal totalAmount;

    Integer payMethod;
}
