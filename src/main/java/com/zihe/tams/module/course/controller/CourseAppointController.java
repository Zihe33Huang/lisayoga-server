package com.zihe.tams.module.course.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zihe.tams.common.base.BaseController;
import com.zihe.tams.common.model.ApiResult;
import com.zihe.tams.module.course.model.dto.*;
import com.zihe.tams.module.course.model.vo.CourseAppointListVO;
import com.zihe.tams.module.course.model.vo.CourseListVO;
import com.zihe.tams.module.course.service.CourseAppointService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huangzihe
 * @date 2022/3/15 7:27 下午
 */
@RestController
@RequestMapping("/appoint")
public class CourseAppointController extends BaseController {
    @Autowired
    CourseAppointService courseAppointService;


    @ApiOperation(value = "分页列表", response = CourseListVO.class)
    @GetMapping
    public ResponseEntity<ApiResult<IPage<CourseAppointListVO>>> pageCourse(CourseAppointPageQuery pageQuery) {

        return success(courseAppointService.pageCourse(pageQuery));
    }

    @PostMapping
    public ResponseEntity<ApiResult<?>> appoint(@RequestBody CourseAppointDTO courseAppointDTO) {
        return successOrFail(courseAppointService.appoint(courseAppointDTO));
    }

    @PostMapping("upWait")
    public ResponseEntity<ApiResult<?>> upWait(@RequestBody CourseAppointDTO courseAppointDTO) {
        return successOrFail(courseAppointService.upWait(courseAppointDTO));
    }

    @DeleteMapping
    public ResponseEntity<ApiResult<?>> cancel(@RequestBody CourseAppointDTO courseAppointDTO) {
        return successOrFail(courseAppointService.cancel(courseAppointDTO));
    }

    @ApiOperation(value = "分页列表", response = CourseListVO.class)
    @GetMapping("/customerCourse")
    public ResponseEntity<ApiResult<IPage<CustomerCourseListVO>>> pageCustomerCourse(CustomerCoursePageQuery pageQuery) {

        return success(courseAppointService.pageCustomerCourse(pageQuery));
    }

    @PostMapping("/signIn")
    public ResponseEntity<ApiResult<Boolean>> signIn(@RequestBody CourseAppointDTO courseAppointDTO) {
        return success(courseAppointService.signIn(courseAppointDTO));
    }

    @DeleteMapping("/signIn")
    public ResponseEntity<ApiResult<Boolean>> cancelSignIn(@RequestBody CourseAppointDTO courseAppointDTO) {
        return success(courseAppointService.cancelSignIn(courseAppointDTO));
    }

    @GetMapping("/appointed/{id}")
    public ResponseEntity<ApiResult<List<CustomerCourseListVO>>> getAppointedCourse(@PathVariable("id") Long customerId) {
        return success(courseAppointService.getAppointedCourse(customerId));
    }


}
