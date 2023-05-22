package com.zihe.tams.module.customer.model.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author huangzihe
 * @date 2022/3/9 11:24 下午
 */
@Data
@TableName("t_customer")
public class CustomerDO {


    @TableId(value = "id", type = IdType.AUTO)
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

    /**
     * 停启用状态
     */
    private Integer enableState;

    private String picUrl;

    private String openId;

    private Integer hasHurt;

    private Integer hasPracticed;

    private String aim;

    private Integer type;

    String channelExt;

    String remark;

    Character isDeleted;

}
