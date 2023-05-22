package com.zihe.tams.module.card.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zihe.tams.module.card.model.data.CardDO;
import com.zihe.tams.module.card.model.dto.CardDTO;

import java.util.List;

/**
 * 会员卡 Service
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
public interface CardService extends IService<CardDO> {

    CardDTO getByCardId(Long cardId);

    List<CardDTO> getCardListBy(Long customerId,Integer status);
}
