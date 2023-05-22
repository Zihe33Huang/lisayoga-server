package com.zihe.tams.module.card.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class GetPersonCardInfoRequest {
    @ApiModelProperty(value = "顾客id")
    @NotBlank(message = "顾客id不可为空")
    private String customerId;

    @ApiModelProperty(value = "卡类别")
    @NotBlank(message = "卡类别不可为空")
    private String cardRuleId;
}
