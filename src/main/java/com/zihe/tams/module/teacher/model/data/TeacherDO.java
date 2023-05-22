package com.zihe.tams.module.teacher.model.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_teacher")
public class TeacherDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 昵称
     */
    private String akaName;

    /**
     * 入职时间
     */
    private Date  enterTime;

    /**
     * 手机号码
     */
    private String phoneNum;



    /**
     * 资质
     */
    private String qualification;

    /**
     * 个人介绍
     */
    private String introduction;


    /**
     * 停启用状态
     */
    private Integer enableState;

    private Integer type;


    private Date birthday;

    private String picUrl;

    private BigDecimal basicSalary;

    private BigDecimal royaltyRate;

    private BigDecimal allowance;

    private String openId;

}
