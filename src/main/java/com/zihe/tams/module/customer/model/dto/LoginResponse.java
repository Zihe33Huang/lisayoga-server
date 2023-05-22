package com.zihe.tams.module.customer.model.dto;

import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/4/20 11:35 下午
 */
@Data
public class LoginResponse {
    String name;

    String akaName;
    // 0、 顾客  1、教练
    Integer type;

    Long id;

    Integer totalCourse;
}
