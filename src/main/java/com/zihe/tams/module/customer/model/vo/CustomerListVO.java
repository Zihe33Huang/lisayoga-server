package com.zihe.tams.module.customer.model.vo;

import com.zihe.tams.module.card.model.response.GetPersonCardInfoResponse;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author huangzihe
 * @date 2022/3/10 8:44 下午
 */
@Data
public class CustomerListVO {

    private Long id;

    private String name;

    private String wechat;

    private Date birthday;

    private Integer age;

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

    private List<String> fileList;

    private List<GetPersonCardInfoResponse> cardList;

    private Integer hasHurt;

    private Integer hasPracticed;

    private String aim;

    private Integer type;

    String channelExt;

    String remark;

    Integer cancelTimes;

}
