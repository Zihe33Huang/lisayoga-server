package com.zihe.tams.module.card.model.data;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员卡流水 entity
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-14
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("card_bill")
public class CardBillDO {

	private static final long serialVersionUID = 1L;
	//region 数据库字段
	public static final String CUSTOMER_ID_FIELD = "customer_id";
	public static final String SOURCE_ID_FIELD = "source_id";
	public static final String CARD_NO_FIELD = "card_no";
	public static final String TYPE_FIELD = "type";
	public static final String PRE_BALANCE_FIELD = "pre_balance";
	public static final String CONSUMPTION_AMOUNT_FIELD = "consumption_amount";
	public static final String AVAILABLE_BALANCE_FIELD = "available_balance";
	public static final String pre_Count = "pre_count";
	public static final String CONSUMPTION_COUNT_FIELD = "consumption_count";
	public static final String AVAILABLE_COUNT_FIELD = "available_count";
	public static final String REMARK_FIELD = "remark";
	/**
	 * id,默认使用数据库自增ID  数据库列名
	 */
	public static final String ID = "id";
	/**
	 * 创建时间  数据库列名
	 */
	public static final String CREATE_TIME = "create_time";
	/**
	 * 更新时间  数据库列名
	 */
	public static final String UPDATE_TIME = "update_time";

	//endregion
	//region 实体字段
	/**
	 * id,默认使用数据库自增ID
	 */
	@TableId(value = ID, type = IdType.AUTO)
	private Long id;

	/**
	 * 创建时间
	 */
	@TableField(value = CREATE_TIME)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(value = UPDATE_TIME)
	private LocalDateTime updateTime;

    /** 客户id */
	@TableField(value = CUSTOMER_ID_FIELD)
	private Long customerId;

    /** 来源编码 */
	@TableField(value = SOURCE_ID_FIELD)
	private Long sourceId;

    /** 卡号 */
	@TableField(value = CARD_NO_FIELD)
	private String cardNo;

    /** 类型 1:充值 2：退款 */
	@TableField(value = TYPE_FIELD)
	private Integer type;

    /** 余额 */
	@TableField(value = PRE_BALANCE_FIELD)
	private BigDecimal preBalance;

    /** 消费金额 */
	@TableField(value = CONSUMPTION_AMOUNT_FIELD)
	private BigDecimal consumptionAmount;

    /** 现有余额 余额减去消费金额 */
	@TableField(value = AVAILABLE_BALANCE_FIELD)
	private BigDecimal availableBalance;

	/** 可用次数 */
	@TableField(value = pre_Count)
	private Integer preCount;

	/** 消费次数 */
	@TableField(value = CONSUMPTION_COUNT_FIELD)
	private Integer consumptionCount;

	/** 现有次数 可用次数减去消费次数 */
	@TableField(value = AVAILABLE_COUNT_FIELD)
	private Integer availableCount;


    /** 备注 */
	@TableField(value = REMARK_FIELD)
	private String remark;

	private Character isDeleted;

}
