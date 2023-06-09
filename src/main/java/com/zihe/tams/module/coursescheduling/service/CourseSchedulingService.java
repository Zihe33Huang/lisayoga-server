package com.zihe.tams.module.coursescheduling.service;

import com.zihe.tams.module.coursescheduling.model.dto.CourseSchedulingBatchSaveDTO;
import com.zihe.tams.module.coursescheduling.model.dto.CourseSchedulingQuery;
import com.zihe.tams.module.coursescheduling.model.dto.CourseSchedulingSaveDTO;
import com.zihe.tams.module.coursescheduling.model.dto.CourseSchedulingTimeUpdateDTO;
import com.zihe.tams.module.coursescheduling.model.vo.CourseSchedulingListVO;
import com.zihe.tams.module.coursescheduling.model.vo.CourseSchedulingReportVO;

import java.util.List;
import java.util.Map;

public interface CourseSchedulingService {

    List<CourseSchedulingListVO> listCourseScheduling(CourseSchedulingQuery query);

    CourseSchedulingListVO getCourseSchedulingById(Long id);

    Map<String, Integer> getCourseSchedulingCourseCount(CourseSchedulingQuery query);

    List<CourseSchedulingReportVO> getReportTeacherCount(String startDate, String endDate);

    CourseSchedulingReportVO getReportOneTeacherCount(Long teacherId, String startDate, String endDate);


    List<CourseSchedulingReportVO> getReportCourseCount(String startDate, String endDate);

    boolean saveCourseScheduling(CourseSchedulingSaveDTO saveDTO);

    void batchSaveCourseScheduling(CourseSchedulingBatchSaveDTO saveDTO);

    boolean updateCourseSchedulingTimeById(Long id, CourseSchedulingTimeUpdateDTO updateDTO);

    boolean updateCourseSchedulingById(Long id, CourseSchedulingSaveDTO saveDTO);

    boolean removeCourseSchedulingById(Long id);

    void removeCourseSchedulingByIdList(List<Long> idList);

}
