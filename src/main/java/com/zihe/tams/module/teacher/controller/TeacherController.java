package com.zihe.tams.module.teacher.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zihe.tams.common.base.BaseController;
import com.zihe.tams.common.exception.BusinessException;
import com.zihe.tams.common.model.ApiResult;
import com.zihe.tams.module.teacher.model.data.TeacherCourseFeeDO;
import com.zihe.tams.module.teacher.model.dto.*;
import com.zihe.tams.module.teacher.model.vo.TeacherListVO;
import com.zihe.tams.module.teacher.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Api(tags = "教师")
@RequestMapping("teacher")
@RestController
public class TeacherController extends BaseController {

    @Autowired
    private TeacherService teacherService;


    @ApiOperation(value = "分页列表", response = TeacherListVO.class)
    @GetMapping
    public ResponseEntity<ApiResult<IPage<TeacherListVO>>> pageTeacher(TeacherPageQuery pageQuery) {

        return success(teacherService.pageTeacher(pageQuery));
    }

    @ApiOperation(value = "参照列表", response = TeacherListVO.class)
    @GetMapping("list/ref")
    public ResponseEntity<ApiResult<List<TeacherListVO>>> refList() {

        return success(teacherService.refList());
    }

    @ApiOperation(value = "详情", response = TeacherListVO.class)
    @GetMapping("{id}")
    public ResponseEntity<ApiResult<TeacherListVO>> getTeacherById(@PathVariable("id") Long id) {

        return success(teacherService.getTeacherById(id));
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public ResponseEntity<ApiResult<?>> saveTeacher(@RequestParam("file") List<MultipartFile> file, TeacherSaveDTO saveDTO, HttpServletRequest request) {
        if (0 > saveDTO.getRoyaltyRate().compareTo(BigDecimal.ZERO) || saveDTO.getRoyaltyRate().compareTo(BigDecimal.valueOf(100)) > 0 ) {
            throw new BusinessException("提成范围在[0%, 100%]");
        }
        saveDTO.setFile(file);
        return successOrFail(teacherService.saveTeacher(saveDTO,request));
    }

    @ApiOperation(value = "修改")
    @PostMapping("{id}")
    public ResponseEntity<ApiResult<?>> updateTeacherById(@RequestParam("file") List<MultipartFile> file, @PathVariable("id") Long id, TeacherUpdateDTO saveDTO) {
        if (0 > saveDTO.getRoyaltyRate().compareTo(BigDecimal.ZERO) || saveDTO.getRoyaltyRate().compareTo(BigDecimal.valueOf(100)) > 0 ) {
            throw new BusinessException("提成范围在[0%, 100%]");
        }
        saveDTO.setFile(file);
        return successOrFail(teacherService.updateTeacherById(id, saveDTO));
    }

    @ApiOperation(value = "停启用")
    @PutMapping("{id}/enable-state/{enableState}")
    public ResponseEntity<ApiResult<?>> updateTeacherEnableStateById(@PathVariable("id") Long id, @PathVariable("enableState") Integer enableState) {

        return successOrFail(teacherService.updateTeacherEnableStateById(id, enableState));
    }

    @GetMapping("course")
    public ResponseEntity<ApiResult<IPage<TeacherCourseDTO>>> pageTeacherCourse(TeacherCourseQuery query) {
        return success(teacherService.queryTeacherCourse(query));
    }

    /**
     * 计算工资
     * @param query
     * @return
     */
    @GetMapping("pay")
    public ResponseEntity<ApiResult<TeacherPayDTO>> pay(TeacherPayQuery query) {
        if ("month".equals(query.getType())) {
            if (query.getMonth() == null || StringUtils.isBlank(query.getMonth())) {
                throw new BusinessException("请选择月份!");
            }
            query.setStartDate("1970-01-01");
            query.setEndDate("1970-01-01");
        } else {
            if (query.getStartDate() == null || query.getEndDate() == null) {
                throw new BusinessException("请选择日期!");
            }
            query.setMonth("1970-01-01");
        }
        return success(teacherService.countPayment(query.getTeacherId(), DateUtil.parse(query.getMonth(), "yyyy-MM-dd"), DateUtil.parse(query.getStartDate(), "yyyy-MM-dd"), DateUtil.parse(query.getEndDate(), "yyyy-MM-dd"), query.getType()));
    }

    @GetMapping("payList")
    public ResponseEntity<ApiResult<List<TeacherPayDTO>>> payList(TeacherListPayQuery query) {
//        if (query.getMonth() == null || StringUtils.isBlank(query.getMonth())) {
//            throw new BusinessException("请选择月份!");
//        }
        return success(teacherService.listTeacherPay(query.getStartDate(), query.getEndDate()));
    }

    @PostMapping("payConfirm")
    public ResponseEntity<ApiResult<?>> payConfirm(@RequestBody TeacherPayConfirmDTO confirmDTO) {
        if (confirmDTO.getMonth() == null) {
            throw new BusinessException("请选择月份!");
        }
        return successOrFail(teacherService.payConfirm(confirmDTO));
    }

    @GetMapping("conf")
    public ResponseEntity<ApiResult<IPage<TeacherCourseFeeDTO>>> listConf(TeacherConfigQuery query) {
        return success(teacherService.listTeacherCourseFee(query));
    }

    @PostMapping("conf")
    public ResponseEntity<ApiResult<?>> changeConf(@RequestBody List<TeacherCourseFeeDO> list) {
        return successOrFail(teacherService.saveTeacherCourseFee(list));
    }

//    @RequestMapping("/up")
//    public JSONObject up(@RequestParam("picFile") MultipartFile picture, HttpServletRequest request) {
//        return teacherService.uploadFile(picture, request);
//    }

}
