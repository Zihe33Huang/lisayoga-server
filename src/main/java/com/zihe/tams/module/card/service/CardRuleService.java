package com.zihe.tams.module.card.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zihe.tams.module.card.model.data.CardRuleDO;
import com.zihe.tams.module.card.model.dto.CardRuleDTO;

/**
 * 卡规则 Service
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
public interface CardRuleService extends IService<CardRuleDO> {
    CardRuleDTO getByRuleId(Long id);
}
