package com.zihe.tams.module.card.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author huangzihe
 * @date 2023/11/17 1:53 AM
 */
@Data
public class CardTypeVO {
    /**
     * Card Type 1. Time Card  2. Visit Card  3. Recharge Card
     */
    Integer type;


    List<CardRuleDTO> list;

}
