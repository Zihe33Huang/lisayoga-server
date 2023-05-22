package com.zihe.tams.module.card.model.data;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author huangzihe
 * @date 2022/4/10 4:47 下午
 */
@Data
public class TeacherRoyaltyDTO {

    String customerName;

    String cardType;

    BigDecimal paymentAmount;

    LocalDate date;

    BigDecimal royaltyRate;

}
