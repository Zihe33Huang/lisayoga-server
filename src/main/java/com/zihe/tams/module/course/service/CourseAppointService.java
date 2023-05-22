package com.zihe.tams.module.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zihe.tams.module.course.model.dto.*;
import com.zihe.tams.module.course.model.vo.CourseAppointListVO;

import java.util.List;

public interface CourseAppointService {


    /**
     * 约课信息
     * @param pageQuery
     * @return
     */
    IPage<CourseAppointListVO> pageCourse(CourseAppointPageQuery pageQuery);


    /**
     * 会员约课
     * @param courseAppointDTO
     * @return
     */
    public boolean appoint(CourseAppointDTO courseAppointDTO);

    public boolean upWait(CourseAppointDTO courseAppointDTO);

    /**
     * 取消约课
     * @param courseAppointDTO
     * @return
     */
    public boolean cancel(CourseAppointDTO courseAppointDTO);


    /**
     * 会员上课信息
     * @param pageQuery
     * @return
     */
    public IPage<CustomerCourseListVO> pageCustomerCourse(CustomerCoursePageQuery pageQuery);

    /**
     * 签到
     * @param courseAppointDTO
     * @return
     */
    public Boolean signIn(CourseAppointDTO courseAppointDTO);

    public Integer getAppointNum(Long scheduleId);

    public Integer getAttendanceNum(Long scheduleId);

    public boolean cancelSignIn(CourseAppointDTO courseAppointDTO);

    public boolean deleteLogic(Long scheduleId);

    public List<CustomerCourseListVO> getAppointedCourse(Long customerId);
}
