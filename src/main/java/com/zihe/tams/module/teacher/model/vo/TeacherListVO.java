package com.zihe.tams.module.teacher.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel(value = "教师列表返回结果")
@Data
public class TeacherListVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "停启用状态")
    private Integer enableState;

    /**
     * 昵称
     */
    private String akaName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 入职时间
     */
    private Date enterTime;

    /**
     * 手机号码
     */
    private String phoneNum;


    /**
     * 照片地址
     */
    private String picUrl;

    /**
     * 资质
     */
    private String qualification;

    /**
     * 个人介绍
     */
    private String introduction;

    private Date birthday;

    private List<String> pictureList;

    private Integer type;

    private String isPaidThisMonth;

    private String isPaidLastMonth;

    BigDecimal basicSalary;

    BigDecimal royaltyRate;

    BigDecimal allowance;
}
