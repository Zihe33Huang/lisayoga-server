package com.zihe.tams.module.teacher.model.convert;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zihe.tams.module.coursescheduling.model.vo.CourseSchedulingListVO;
import com.zihe.tams.module.teacher.model.data.TeacherCourseFeeDO;
import com.zihe.tams.module.teacher.model.data.TeacherDO;
import com.zihe.tams.module.teacher.model.data.TeacherSalaryDO;
import com.zihe.tams.module.teacher.model.dto.*;
import com.zihe.tams.module.teacher.model.vo.TeacherListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public abstract class AbstractTeacherConverter {

    public static AbstractTeacherConverter INSTANCE = Mappers.getMapper(AbstractTeacherConverter.class);

    public abstract Page<TeacherListVO> doPage2ListVoPage(IPage<TeacherDO> doPage);

    public abstract List<TeacherListVO> doList2ListVoList(List<TeacherDO> doList);

    public abstract TeacherListVO do2ListVO(TeacherDO dataObj);

    public abstract TeacherDO saveDto2DO(TeacherSaveDTO saveDTO);

    public abstract TeacherDO saveDto2DO(TeacherUpdateDTO saveDTO);

    public abstract TeacherPayDTO do2dto(TeacherSalaryDO salaryDO);

    public abstract TeacherSalaryDO dto2do(TeacherPayConfirmDTO confirmDTO);

    public abstract List<TeacherPayDTO> do2dtoList(List<TeacherSalaryDO> salaryDO);

    public abstract List<TeacherCourseFeeDTO> do2dtoList2(List<TeacherCourseFeeDO> salaryDO);

    public abstract List<SchedulingCourseFeeDTO>   do2dtoList3(List<CourseSchedulingListVO> listVOS);

}
