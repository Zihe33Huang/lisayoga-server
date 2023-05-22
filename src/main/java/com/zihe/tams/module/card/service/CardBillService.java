package com.zihe.tams.module.card.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zihe.tams.module.card.model.data.CardBillDO;
import com.zihe.tams.module.card.model.dto.CardBillDTO;

import java.util.List;

/**
 * 会员卡流水 Service
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
public interface CardBillService extends IService<CardBillDO> {

    CardBillDTO getNowBill(String cardNo);

    CardBillDTO getNowBill(Long cardId);

    CardBillDTO getBillBySourceIdAndCustomerId(Long sourceId, Long customerId);

    List<CardBillDTO> getBillList(String cardNo);

    Boolean deleteBillLogic(Long scheduleId, Long customerId);
}
