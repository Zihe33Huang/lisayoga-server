package com.zihe.tams.module.card.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author huangzihe
 * @date 2022/4/30 1:34 上午
 */
@Data
public class CardDateChangeDTO {
    Long cardId;

    // 0 为开始时间  1为结束时间
    Integer type;

    Date date;
}
