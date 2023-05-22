package com.zihe.tams.module.customer.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author huangzihe
 * @date 2022/3/10 10:40 下午
 */
@Data
public class CustomerSaveDTO {
    private Long id;

    private String name;

    private String wechat;

    private Date birthday;

    private String phoneNum;

    private Date enterTime;

    /**
     * 了解渠道
     */
    private Integer channel;

    private String weight;

    private String height;
    private Integer hasHurt;

    private Integer hasPracticed;

    private String aim;

    private Integer type;

    String channelExt;

    String remark;
}
