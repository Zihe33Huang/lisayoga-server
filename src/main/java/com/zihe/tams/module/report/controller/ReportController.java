package com.zihe.tams.module.report.controller;

import com.zihe.tams.common.base.BaseController;
import com.zihe.tams.common.model.ApiResult;
import com.zihe.tams.module.card.service.RechargeService;
import com.zihe.tams.module.coursescheduling.model.vo.CourseSchedulingReportVO;
import com.zihe.tams.module.coursescheduling.service.CourseSchedulingService;
import com.zihe.tams.module.report.dto.response.GetRechargeReportResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "报表")
@RequestMapping("report")
@RestController
public class ReportController extends BaseController {

    @Autowired
    private CourseSchedulingService courseSchedulingService;

    @Autowired
    private RechargeService rechargeService;

    @ApiOperation(value = "老师上课数量", response = CourseSchedulingReportVO.class)
    @GetMapping("teacher/count")
    public ResponseEntity<ApiResult<List<CourseSchedulingReportVO>>> getReportTeacherCount(@RequestParam("startDate") String startDate,
                                                                                           @RequestParam("endDate") String endDate) {

        return success(courseSchedulingService.getReportTeacherCount(startDate, endDate));
    }

    @ApiOperation(value = "课程数量", response = CourseSchedulingReportVO.class)
    @GetMapping("course/count")
    public ResponseEntity<ApiResult<List<CourseSchedulingReportVO>>> getReportCourseCount(@RequestParam("startDate") String startDate,
                                                                                          @RequestParam("endDate") String endDate) {

        return success(courseSchedulingService.getReportCourseCount(startDate, endDate));
    }

    @ApiOperation(value = "主营业务记录")
    @GetMapping("recharge/count")
    public ResponseEntity<ApiResult<List<GetRechargeReportResponse>>> getReportRechargeCount(@RequestParam("startDate") String startDate,
                                                                                             @RequestParam("endDate") String endDate) {

        return success(rechargeService.getReportRechargeCount(startDate,endDate));
    }
}
