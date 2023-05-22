package com.zihe.tams.module.card.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zihe.tams.module.card.model.data.RechargeDO;
import com.zihe.tams.module.card.model.dto.RechargeDTO;
import com.zihe.tams.module.report.dto.response.GetRechargeReportResponse;

import java.util.List;

/**
 * 会员卡充值记录 Service
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
public interface RechargeService extends IService<RechargeDO> {
    RechargeDTO getPersonRechargeDO(String cardNo);

    List<GetRechargeReportResponse> getReportRechargeCount(String startDate, String endDate);

    /**
     * 获取一张卡的全部充值额度  (时长卡 次卡的size为1， 充值卡可能有多次)
     * @param cardNo
     * @return
     */
    public List<GetRechargeReportResponse> getAllRecharge(String cardNo);
    }
