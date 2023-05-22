package com.zihe.tams.module.card.dao;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zihe.tams.module.card.model.data.CardBillDO;
import com.zihe.tams.module.card.model.data.TeacherRoyaltyDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员卡流水 dao
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@Mapper
public interface CardBillMapper extends BaseMapper<CardBillDO> {
    /**
     * 逻辑删除
     * @param scheduleId
     * @return
     */
    public boolean deleteBillByScheduleId(@Param("scheduleId") Long scheduleId, @Param("customerId") Long customerId);

    public List<TeacherRoyaltyDTO> selectRechargeByTeacherId(@Param("teacherId") Long teacherId, @Param("startDate") String start, @Param("endDate") String end);

    public List<TeacherRoyaltyDTO> selectRefundByTeacherId(@Param("teacherId") Long teacherId, @Param("startDate") String start, @Param("endDate") String end);

}
