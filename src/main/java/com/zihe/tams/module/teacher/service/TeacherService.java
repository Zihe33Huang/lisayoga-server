package com.zihe.tams.module.teacher.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zihe.tams.module.teacher.model.data.TeacherCourseFeeDO;
import com.zihe.tams.module.teacher.model.dto.*;
import com.zihe.tams.module.teacher.model.vo.TeacherListVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public interface TeacherService {

    IPage<TeacherListVO> pageTeacher(TeacherPageQuery pageQuery);

    List<TeacherListVO> refList();

    TeacherListVO getTeacherById(Long id);

    boolean saveTeacher(TeacherSaveDTO saveDTO, HttpServletRequest request);

    boolean updateTeacherById(Long id, TeacherUpdateDTO saveDTO);

    boolean updateTeacherEnableStateById(Long id, Integer enableState);

    //    public JSONObject uploadFile(MultipartFile picture, HttpServletRequest request);
    public IPage<TeacherCourseDTO> queryTeacherCourse(TeacherCourseQuery query);

    public TeacherPayDTO countPayment(Long teacherId, Date day, Date startDay, Date endDay, String type);

    public boolean payConfirm(TeacherPayConfirmDTO confirmDTO);

    public List<TeacherPayDTO> listTeacherPay(String startDate, String endDate);

    public void addCourse(Long courseId);

    public void addTeacher(Long teacherId);

    public IPage<TeacherCourseFeeDTO> listTeacherCourseFee(TeacherConfigQuery query);

    public boolean saveTeacherCourseFee(List<TeacherCourseFeeDO> list);

}
