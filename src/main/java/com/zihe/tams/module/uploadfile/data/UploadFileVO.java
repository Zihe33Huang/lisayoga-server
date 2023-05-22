package com.zihe.tams.module.uploadfile.data;

import lombok.Data;

/**
 * @author huangzihe
 * @date 2022/3/28 2:59 下午
 */
@Data
public class UploadFileVO {
    private Long id;

    private String fileName;

    private String url;

    private Long associateId;

    /**
     * 1、 会员 2、教练
     */
    private Integer type;
}
