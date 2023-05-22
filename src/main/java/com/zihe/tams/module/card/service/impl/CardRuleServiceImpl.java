package com.zihe.tams.module.card.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zihe.tams.common.util.ConvertUtils;
import com.zihe.tams.module.card.dao.CardRuleMapper;
import com.zihe.tams.module.card.model.data.CardRuleDO;
import com.zihe.tams.module.card.model.dto.CardRuleDTO;
import com.zihe.tams.module.card.service.CardRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 卡规则 ServiceImpl
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@Slf4j
@Service
public class CardRuleServiceImpl extends ServiceImpl<CardRuleMapper, CardRuleDO> implements CardRuleService {

    @Override
    public CardRuleDTO getByRuleId(Long id) {
        return ConvertUtils.of(this.getById(id),CardRuleDTO.class);
    }

}
   