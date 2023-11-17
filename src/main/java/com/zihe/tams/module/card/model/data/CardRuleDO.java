package com.zihe.tams.module.card.model.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 卡规则 entity
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
@TableName("card_rule")
public class CardRuleDO {

	private static final long serialVersionUID = 1L;
	//region 数据库字段
	public static final String NAME_FIELD = "name";
	public static final String VALIDITY_PERIOD_FIELD = "validity_period";
	public static final String PERIOD_FIELD = "period";
	public static final String FREQUENCY_FIELD = "frequency";
	public static final String IS_GROUP_CLASS_FIELD = "is_group_class";
	public static final String IS_PRIVATE_EDUCATION_FIELD = "is_private_education";
	public static final String IS_PRIVATE_PILATES_FIELD = "is_private_pilates";
	public static final String IS_RECHARGEABLE_CARD_FIELD = "is_rechargeable_card";
	public static final String DISCOUNT_RATE_FIELD = "discount_rate";
	public static final String IS_SPECIAL_RECHARGEABLE_CARD_FIELD = "is_special_rechargeable_card";
	public static final String IS_SPECIAL_SELF_FIELD = "is_special_self";
	public static final String TYPE_FIELD = "type";
	public static final String ID = "id";
	//endregion
	//region 实体字段

	@TableId(value = ID, type = IdType.AUTO)
	private Long id;


    /** 名称 */
	@TableField(value = NAME_FIELD)
	private String name;

    /** 时长 */
	@TableField(value = VALIDITY_PERIOD_FIELD)
	private Integer validityPeriod;

    /** 时间段 1: 工作日 2: 周末 */
	@TableField(value = PERIOD_FIELD)
	private Integer period;


    /** 优惠额度 */
	@TableField(value = DISCOUNT_RATE_FIELD)
	private String discountRate;



	@TableField(value = TYPE_FIELD)
	private Integer type;

	private Integer frequency;



}
