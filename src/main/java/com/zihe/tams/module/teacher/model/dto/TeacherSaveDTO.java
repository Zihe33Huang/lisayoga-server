package com.zihe.tams.module.teacher.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@ApiModel(value = "教师分页查询参数")
@Data
public class TeacherSaveDTO {

    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     * 昵称
     */
    private String akaName;


    /**
     * 入职时间
     */
    private Date enterTime;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 手机号码
     */
    private String phoneNum;


    /**
     * 照片地址
     */
    private String pictureUrl;

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

    @JsonIgnore
    private List<MultipartFile> file;

    private List<String> pictureUrlList;

    /**
     * 0 全职 1兼职
     */
    private Integer type;

    private BigDecimal basicSalary;

    private BigDecimal royaltyRate;

    private BigDecimal allowance;
}
