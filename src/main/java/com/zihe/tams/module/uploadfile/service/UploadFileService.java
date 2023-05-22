package com.zihe.tams.module.uploadfile.service;

import com.zihe.tams.module.uploadfile.data.UploadFileDO;

import java.util.List;

/**
 * @author huangzihe
 * @date 2022/3/28 2:52 下午
 */
public interface UploadFileService {
    public List<UploadFileDO> list(Long id);

    public Boolean delete(Long id);
}
