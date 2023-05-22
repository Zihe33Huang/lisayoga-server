package com.zihe.tams.module.card.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CardRulePeriodEnum {
    WEEKDAY(1, "工作日"), WEEKEND(2, "周末");
    private Integer code;
    private String desc;

}
