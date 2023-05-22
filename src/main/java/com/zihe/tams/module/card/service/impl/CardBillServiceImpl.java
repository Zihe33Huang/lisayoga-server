package com.zihe.tams.module.card.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zihe.tams.common.util.CollectionUtils;
import com.zihe.tams.common.util.ConvertUtils;
import com.zihe.tams.module.card.dao.CardBillMapper;
import com.zihe.tams.module.card.model.data.CardBillDO;
import com.zihe.tams.module.card.model.dto.CardBillDTO;
import com.zihe.tams.module.card.service.CardBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员卡流水 ServiceImpl
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@Slf4j
@Service
public class CardBillServiceImpl extends ServiceImpl<CardBillMapper, CardBillDO> implements CardBillService {

    @Override
    public CardBillDTO getNowBill(String cardNo) {
        List<CardBillDO> cardBillDOS = this.baseMapper.selectList(new LambdaQueryWrapper<CardBillDO>()
                .eq(CardBillDO::getCardNo,cardNo).eq(CardBillDO::getIsDeleted, 'N')
                .orderByDesc(CardBillDO::getCreateTime));
        if (CollectionUtils.isEmpty(cardBillDOS)){
            return null;
        }
        return ConvertUtils.of(cardBillDOS.get(0),CardBillDTO.class);
    }
    @Override
    public CardBillDTO getNowBill(Long cardId) {
        List<CardBillDO> cardBillDOS = this.baseMapper.selectList(new LambdaQueryWrapper<CardBillDO>()
                .eq(CardBillDO::getId,cardId).eq(CardBillDO::getIsDeleted, 'N')
                .orderByDesc(CardBillDO::getCreateTime));
        if (CollectionUtils.isEmpty(cardBillDOS)){
            return null;
        }
        return ConvertUtils.of(cardBillDOS.get(0),CardBillDTO.class);
    }

    @Override
    public CardBillDTO getBillBySourceIdAndCustomerId(Long sourceId, Long customerId) {
        List<CardBillDO> cardBillDOS = this.baseMapper.selectList(new LambdaQueryWrapper<CardBillDO>()
                .eq(CardBillDO::getSourceId, sourceId).eq(CardBillDO::getIsDeleted, 'N').eq(CardBillDO::getCustomerId, customerId)
                .orderByDesc(CardBillDO::getCreateTime));
        if (CollectionUtils.isEmpty(cardBillDOS)){
            return null;
        }
        return ConvertUtils.of(cardBillDOS.get(0),CardBillDTO.class);
    }


    @Override
    public List<CardBillDTO> getBillList(String cardNo) {

        return ConvertUtils.ofList(this.getBaseMapper().selectList(new LambdaQueryWrapper<CardBillDO>().eq(CardBillDO::getIsDeleted, 'N').eq(CardBillDO::getCardNo,cardNo).orderByDesc(CardBillDO::getCreateTime)),CardBillDTO.class);
    }

    @Override
    public Boolean deleteBillLogic(Long scheduleId, Long customerId) {
        return this.baseMapper.deleteBillByScheduleId(scheduleId, customerId);
    }

}
