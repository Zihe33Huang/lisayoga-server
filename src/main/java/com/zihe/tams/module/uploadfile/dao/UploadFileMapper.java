package com.zihe.tams.module.uploadfile.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zihe.tams.module.uploadfile.data.UploadFileDO;
import org.apache.ibatis.annotations.Param;

/**
 * @author huangzihe
 * @date 2022/3/28 2:39 下午
 */
public interface UploadFileMapper extends BaseMapper<UploadFileDO> {
    boolean deleteLogic(@Param("fileId") Long fileId);
}
