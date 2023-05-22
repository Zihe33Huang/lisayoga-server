package com.zihe.tams.module.course.model.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author huangzihe
 * @date 2022/4/3 6:21 下午
 */
@Getter
@AllArgsConstructor
public enum EndEnum {
    B(0, "B端"), C(1, "C端");
    private Integer code;
    private String desc;
}
