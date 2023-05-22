package com.zihe.tams.module.customer.model.dto;

import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/4/14 11:44 下午
 */
@Data
public class LoginRequest {
    String phoneNum;

    String code;
}
