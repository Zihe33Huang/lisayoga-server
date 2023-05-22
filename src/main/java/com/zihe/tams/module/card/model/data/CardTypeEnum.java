package com.zihe.tams.module.card.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CardTypeEnum {

    TIME(1, "时长卡"), NUMBER(2, "次卡"), RECHARGE(3, "充值卡");

    private Integer code;

    private String desc;
}
