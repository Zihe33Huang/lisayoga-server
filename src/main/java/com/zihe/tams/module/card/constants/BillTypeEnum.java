package com.zihe.tams.module.card.constants;

import lombok.Getter;

@Getter
public enum BillTypeEnum {

    RECHARGE("充值",1),
    REFUND("退款",2),
    COURSE("上课扣费", 3)
    ;


    @Getter
    private String name;
    @Getter
    private Integer type;

    BillTypeEnum(String name , Integer type ){
        this.name = name;
        this.type = type;
    }

}
