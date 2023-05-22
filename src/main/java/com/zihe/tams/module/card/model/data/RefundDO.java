package com.zihe.tams.module.card.model.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员卡退款记录 entity
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
@TableName("refund")
public class RefundDO {

	private static final long serialVersionUID = 1L;
	//region 数据库字段
	public static final String CUSTOMER_ID_FIELD = "customer_id";
	public static final String CARD_NO_FIELD = "card_no";
	public static final String CATEGORY_NAME_FIELD = "category_name";
	public static final String TOTAL_PRICE_FIELD = "total_price";
	public static final String TOTAL_TIMES_FIELD = "total_times";
	public static final String AVERAGEPRICE_FIELD = "average_price";
	public static final String ACTUAL_AMOUNT_FIELD = "actual_amount";
	public static final String REFUND_TIMES_FIELD = "refund_times";
	public static final String REFUND_AMOUNT_FIELD = "refund_amount";
	public static final String CHARGE_RATE_FIELD = "charge_rate";
	public static final String REFUND_CHARGE_FIELD = "refund_charge";
	public static final String ACTUAL_REFUND_FIELD = "actual_refund";
	public static final String REFUND_METHOD_FIELD = "refund_method";
	public static final String REMARK_FIELD = "remark";
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
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

    /** 客户id */
	private Long customerId;

    /** 卡号 */
	private String cardNo;


    /** 总价 */
	private BigDecimal totalPrice;

    /** 平均价格 */
	private BigDecimal averagePrice;



    /** 退款次数 */
	private Integer refundCount;


    /** 手续费比例 */
	private BigDecimal chargeRate;

    /** 手续费 */
	private BigDecimal serviceCharge;



    /** 备注 */
	private String remark;
	/** 实际退款 */
	private BigDecimal actualRefundAmount;

	/** 手续费前应退款 */
	private BigDecimal shouldRefundAmount;

	private Integer refundMethod;
	//endregion
}
