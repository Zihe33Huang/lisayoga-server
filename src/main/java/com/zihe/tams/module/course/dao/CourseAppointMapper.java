package com.zihe.tams.module.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zihe.tams.module.course.model.data.CourseAppointDO;
import com.zihe.tams.module.course.model.data.CourseDO;
import com.zihe.tams.module.course.model.dto.CustomerCourseListVO;
import com.zihe.tams.module.customer.model.dto.CustomerAppointDTO;
import com.zihe.tams.module.teacher.model.dto.TeacherCourseDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CourseAppointMapper extends BaseMapper<CourseAppointDO> {

    public int getCurrentAppointSize(Long scheduleId);

    public CourseAppointDO selectByScheduleIdAndCustomerId(@Param("scheduleId") Long scheduleId, @Param("customerId") Long customerId);

    public boolean cancelAppoint(@Param("scheduleId") Long scheduleId, @Param("customerId") Long customerId);

    public CourseAppointDO selectFirstWait(@Param("scheduleId") Long scheduleId);

    public boolean signIn(@Param("scheduleId") Long scheduleId, @Param("customerId")Long customerId);

    public boolean cancelSignIn(@Param("scheduleId") Long scheduleId, @Param("customerId")Long customerId);

    public List<CustomerCourseListVO> pageCustomerCourse(@Param("customerId") Long customerId, @Param("page")Integer page, @Param("size") Integer size);


    public List<CustomerCourseListVO> getAppointedInfo(@Param("customerId") Long customerId);

    public int getTotal(@Param("customerId") Long customerId);

    public int getAppointNum(@Param("scheduleId") Long scheduleId);

    public int getAttendanceNum(@Param("scheduleId") Long scheduleId);

    public int getCourseInPeriodCount(@Param("cardId") Long cardId, @Param("begin")Date begin, @Param("end") Date end);

    public int getTeacherCourseInPeriodCount(@Param("teacherId") Long teacherId, @Param("begin")Date begin, @Param("end") Date end);


    public boolean deleteLogic(Long scheduleId);

    public List<TeacherCourseDTO> queryTeacherCourse(@Param("teacherId") Long teacherId,@Param("startDate") String start, @Param("endDate") String end,  @Param("page")Integer page, @Param("size") Integer size);

    // 查询所有的已约、候补次数，目的是，让C端在选完课后，显示的可用次数减少
    public int countAppointed(@Param("cardId") Long cardId);

    // 查询所有的已约、候补课的价格，可用类型，目的是，让C端在选完课后，显示的可用次数减少
    public List<CourseDO> countAppointedForRecharge(@Param("cardId") Long cardId);

    public boolean upWait(@Param("scheduleId") Long scheduleId, @Param("customerId") Long customerId, @Param("appointTime")Date time);

    /**
     * 该学生上过该老师的课程数
     * @param customerId
     * @param teacherId
     * @return
     */
    public int attendNum(@Param("customerId")Long customerId, @Param("teacherId") Long teacherId);

    public List<CustomerAppointDTO> getCustomerByScheduleId(@Param("scheduleId") Long scheduleId);

    public boolean addCancelTime(@Param("cardId") Long cardId);

}
