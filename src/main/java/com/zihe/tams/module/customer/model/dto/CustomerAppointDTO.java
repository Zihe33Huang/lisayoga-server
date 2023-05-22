package com.zihe.tams.module.customer.model.dto;

import com.zihe.tams.module.customer.model.data.CustomerDO;
import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/5/31 1:39 PM
 */
@Data
public class CustomerAppointDTO extends CustomerDO {
    Integer state;

    Long cardId;
}
