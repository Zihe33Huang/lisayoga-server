package com.zihe.tams.module.uploadfile.data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/3/28 2:38 下午
 */
@TableName("t_upload_file")
@Data
public class UploadFileDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String fileName;

    private String url;

    private Long associateId;

    /**
     * 1、 会员 2、教练
     */
    private Integer type;
}
