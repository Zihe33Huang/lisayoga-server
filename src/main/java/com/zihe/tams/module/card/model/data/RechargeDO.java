package com.zihe.tams.module.card.model.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员卡充值记录 entity
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
@TableName("recharge")
public class RechargeDO  {

	private static final long serialVersionUID = 1L;
	//region 数据库字段
	public static final String CUSTOMER_ID_FIELD = "customer_id";
	public static final String CARD_NO_FIELD = "card_no";
	public static final String PAYMENT_AMOUNT_FIELD = "payment_amount";
	public static final String PAY_METHOD_FIELD = "pay_method";
	public static final String CHANNEL_FEE_FIELD = "channel_fee";
	public static final String ACTUAL_AMOUNT_FIELD = "actual_amount";
	public static final String REMARK_FIELD = "remark";
	public static final String CARD_TIMES = "card_times";
	public static final String validity_Period = "validity_period";
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

    /** 卡号 */
	@TableField(value = CARD_NO_FIELD)
	private String cardNo;

    /** 支付金额 */
	@TableField(value = PAYMENT_AMOUNT_FIELD)
	private BigDecimal paymentAmount;

	/** 充值次数 */
	@TableField(value = CARD_TIMES)
	private Integer cardTimes;

    /** 支付方式 */
	@TableField(value = PAY_METHOD_FIELD)
	private String payMethod;

    /** 渠道费用 */
	@TableField(value = CHANNEL_FEE_FIELD)
	private BigDecimal channelFee;

    /** 实际费用 */
	@TableField(value = ACTUAL_AMOUNT_FIELD)
	private BigDecimal actualAmount;

	/** 充值天数 */
	@TableField(value = validity_Period)
	private Integer validityPeriod;

    /** 备注 */
	@TableField(value = REMARK_FIELD)
	private String remark;

	private Character isDeleted;

	//endregion
}
