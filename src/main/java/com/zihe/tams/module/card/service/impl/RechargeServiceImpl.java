package com.zihe.tams.module.card.service.impl;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zihe.tams.common.util.CollectionUtils;
import com.zihe.tams.common.util.ConvertUtils;
import com.zihe.tams.module.card.dao.RechargeMapper;
import com.zihe.tams.module.card.model.data.RechargeDO;
import com.zihe.tams.module.card.model.dto.RechargeDTO;
import com.zihe.tams.module.card.service.RechargeService;
import com.zihe.tams.module.report.dto.response.GetRechargeReportResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员卡充值记录 ServiceImpl
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@Slf4j
@Service
public class RechargeServiceImpl extends ServiceImpl<RechargeMapper, RechargeDO> implements RechargeService {

    @Override
    public RechargeDTO getPersonRechargeDO(String cardNo) {
        List<RechargeDO> rechargeDOList = this.baseMapper.selectList(new LambdaQueryWrapper<RechargeDO>().eq(RechargeDO::getCardNo,cardNo)
//                .eq(RechargeDO::getIsDeleted,'N')    这里不要
                .orderByDesc(RechargeDO::getCreateTime));
        if (CollectionUtils.isEmpty(rechargeDOList)){
            return null;
        }
        return ConvertUtils.of(rechargeDOList.get(0),RechargeDTO.class);
    }

    @Override
    public List<GetRechargeReportResponse> getReportRechargeCount(String startDate,String endDate) {
        DateTime end = DateUtil.parse(endDate, "yyyy-MM-dd");
        DateTime dateTime = DateUtil.endOfDay(end);
        List<GetRechargeReportResponse> reportRechargeCount = this.baseMapper.getReportRechargeCount(startDate, dateTime.toString("yyyy-MM-dd HH:mm:ss"));
        reportRechargeCount.forEach(dto -> {
            dto.setType("入账");
        });
        List<GetRechargeReportResponse> reportRefundCount = this.baseMapper.getReportRefundCount(startDate, dateTime.toString("yyyy-MM-dd HH:mm:ss"));
        reportRefundCount.forEach(dto -> {
            dto.setType("出账");
        });
        reportRechargeCount.addAll(reportRefundCount);
        return reportRechargeCount;
    }

    @Override
    public List<GetRechargeReportResponse> getAllRecharge(String cardNo) {
        List<GetRechargeReportResponse> allRecharge = this.baseMapper.getAllRecharge(cardNo);
        return allRecharge;
    }
}
