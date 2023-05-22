package com.zihe.tams.module.color.controller;

import com.zihe.tams.common.base.BaseController;
import com.zihe.tams.common.model.ApiResult;
import com.zihe.tams.module.color.model.vo.ColorListVO;
import com.zihe.tams.module.color.service.ColorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "颜色")
@RequestMapping("color")
@RestController
public class ColorController extends BaseController {

    @Autowired
    private ColorService colorService;

    @ApiOperation(value = "有效颜色列表", response = ColorListVO.class)
    @GetMapping("list/effective")
    public ResponseEntity<ApiResult<List<String>>> getEffectiveList() {

        return success(colorService.getEffectiveList());
    }
}
