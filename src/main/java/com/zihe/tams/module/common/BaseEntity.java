package com.zihe.tams.module.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 基础实体类
 * 所有实体都需要继承
 *
 * @author zhouxiaode@sanyygp.com
 * @version 1.0.0
 * @date 2021年11月9日
 */
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 8243162602142898786L;


    /**
     * id,默认使用数据库自增ID
     */
    @TableId(value = ID, type = IdType.AUTO)
    private Long id;


    /**
     * 是否有效；0：否，1：是
     */
    @TableLogic(value = "1", delval = "0")
    @TableField(value = ACTIVE)
    private Integer active = 1;


    //region 数据库字段

    /**
     * id,默认使用数据库自增ID  数据库列名
     */
    public static final String ID = "id";
    /**
     * 创建人姓名/昵称 uc_user.nickname  数据库列名
     */
    public static final String CREATOR = "creator";
    /**
     * 创建人编码 uc_user.user_code  数据库列名
     */
    public static final String CREATOR_CODE = "creator_code";
    /**
     * 创建时间  数据库列名
     */
    public static final String CREATE_TIME = "create_time";
    /**
     * 更新人姓名/昵称 uc_user.nickname  数据库列名
     */
    public static final String UPDATER = "updater";
    /**
     * 更新人编码 uc_user.user_code  数据库列名
     */
    public static final String UPDATER_CODE = "updater_code";
    /**
     * 更新时间  数据库列名
     */
    public static final String UPDATE_TIME = "update_time";

    /**
     * 是否有效；0：否，1：是  数据库列名
     */
    public static final String ACTIVE = "active";


    //endregion


}