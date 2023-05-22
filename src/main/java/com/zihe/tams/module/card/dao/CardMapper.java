package com.zihe.tams.module.card.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zihe.tams.module.card.model.data.CardDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员卡 dao
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@Mapper
public interface CardMapper extends BaseMapper<CardDO> {

}
