package com.zihe.tams.module.card.model.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员卡 entity
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
@TableName("card")
public class CardDO{

	private static final long serialVersionUID = 1L;
	//region 数据库字段
	public static final String CUSTOMER_ID_FIELD = "customer_id";
	public static final String CARD_RULE_ID_FIELD = "card_rule_id";
	public static final String CARD_NO_FIELD = "card_no";
	public static final String REMARK_FIELD = "remark";
	public static final String BALANCE_FIELD = "balance";
	public static final String STARK_TIME_FIELD = "start_time";
	public static final String VALIDITY_PERIOD_FIELD = "validity_period";
	public static final String CARD_TIMES = "card_times";
	public static final String validity_Count = "validity_count";
	public static final String STATUS = "status";

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

    /** 卡规则id */
	@TableField(value = CARD_RULE_ID_FIELD)
	private Long cardRuleId;

    /** 卡号 */
	@TableField(value = CARD_NO_FIELD)
	private String cardNo;

    /** 备注 */
	@TableField(value = REMARK_FIELD)
	private String remark;


    /** 余额 */
	@TableField(value = BALANCE_FIELD)
	private BigDecimal balance;

    /** 开始时间 */
	@TableField(value = STARK_TIME_FIELD)
	private LocalDateTime startTime;

    /** 可用次数 */
	@TableField(value = validity_Count)
	private Integer validityCount;

	@TableField(value = VALIDITY_PERIOD_FIELD)
	private Integer validityPeriod;

	@TableField(value = STATUS)
	private Integer status;

	@TableField("teacher_id")
	private Long teacherId;

	private Integer enableState;
	//endregion

	private Integer cancelTimes;
}
