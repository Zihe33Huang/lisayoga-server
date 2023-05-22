package com.zihe.tams.module.uploadfile.controller;

import com.zihe.tams.common.base.BaseController;
import com.zihe.tams.common.model.ApiResult;
import com.zihe.tams.module.uploadfile.data.UploadFileDO;
import com.zihe.tams.module.uploadfile.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huangzihe
 * @date 2022/3/28 3:03 下午
 */
@RestController
@RequestMapping("file")
public class UploadFileController extends BaseController {
    @Autowired
    UploadFileService uploadFileService;

    @GetMapping("{id}")
    public List<UploadFileDO> list(@PathVariable("id") Long id) {
        return uploadFileService.list(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResult<?>> delete(@PathVariable("id") Long id) {
        return successOrFail(uploadFileService.delete(id));
    }
}
