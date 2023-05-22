package com.zihe.tams.module.card.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author huangzihe
 * @date 2022/4/18 7:08 上午
 */
@Data
public class RechargeCardConfigDTO {

    BigDecimal value;

    List<Integer> discount;

    Boolean edit;
}
