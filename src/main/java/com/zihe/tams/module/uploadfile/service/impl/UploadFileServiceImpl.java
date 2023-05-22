package com.zihe.tams.module.uploadfile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zihe.tams.common.exception.BusinessException;
import com.zihe.tams.module.uploadfile.dao.UploadFileMapper;
import com.zihe.tams.module.uploadfile.data.UploadFileDO;
import com.zihe.tams.module.uploadfile.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author huangzihe
 * @date 2022/3/28 2:53 下午
 */
@Service
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFileDO> implements UploadFileService {

    @Autowired
    UploadFileMapper uploadFileMapper;


    @Override
    public List<UploadFileDO> list(Long customerId) {
        QueryWrapper<UploadFileDO> wrapper = new QueryWrapper<>();
        wrapper.eq("associate_id", customerId).eq("is_deleted", 'N');
        List<UploadFileDO> uploadFileDOS = uploadFileMapper.selectList(wrapper);
        return uploadFileDOS;
    }

    /**
     * 根据UploadFIle_id删除对应记录
     * @param uploadFileId
     * @return
     */
    @Override
    public Boolean delete(Long uploadFileId) {
        boolean isOk = uploadFileMapper.deleteLogic(uploadFileId);
        if (isOk) {
            return true;
        } else {
            throw new BusinessException("删除失败，请联系管理员!");
        }
    }
}
