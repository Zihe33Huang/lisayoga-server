package com.zihe.tams.module.card.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zihe.tams.common.util.ConvertUtils;
import com.zihe.tams.module.card.dao.CardMapper;
import com.zihe.tams.module.card.model.data.CardDO;
import com.zihe.tams.module.card.model.dto.CardDTO;
import com.zihe.tams.module.card.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员卡 ServiceImpl
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@Slf4j
@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, CardDO> implements CardService {

    @Override
    public CardDTO getByCardId(Long cardId) {
        return ConvertUtils.of(this.getById(cardId),CardDTO.class);
    }

    @Override
    public List<CardDTO> getCardListBy(Long customerId,Integer status) {
        return ConvertUtils.ofList(this.getBaseMapper().selectList(new LambdaQueryWrapper<CardDO>().eq(CardDO::getCustomerId,customerId).eq(CardDO::getStatus,status)),CardDTO.class);
    }

}
