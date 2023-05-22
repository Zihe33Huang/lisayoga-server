package com.zihe.tams.module.card.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zihe.tams.module.card.model.data.RechargeDO;
import com.zihe.tams.module.report.dto.response.GetRechargeReportResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员卡充值记录 dao
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@Mapper
public interface RechargeMapper extends BaseMapper<RechargeDO> {
    List<GetRechargeReportResponse> getReportRechargeCount(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<GetRechargeReportResponse> getReportRefundCount(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<GetRechargeReportResponse> getAllRecharge(@Param("cardNo") String cardNo);
}
