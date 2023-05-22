package com.zihe.tams.module.card.model.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class GetPersonInfoRequest {
    @NotBlank(message = "id不为空")
    private String customerId;

}
