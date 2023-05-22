package com.zihe.tams.module.course.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zihe.tams.common.consts.EnableStateEnum;
import com.zihe.tams.common.exception.BusinessException;
import com.zihe.tams.module.course.dao.CourseMapper;
import com.zihe.tams.module.course.model.convert.AbstractCourseConverter;
import com.zihe.tams.module.course.model.data.CourseDO;
import com.zihe.tams.module.course.model.dto.CoursePageQuery;
import com.zihe.tams.module.course.model.dto.CourseSaveDTO;
import com.zihe.tams.module.course.model.vo.CourseListVO;
import com.zihe.tams.module.course.service.CourseService;
import com.zihe.tams.module.teacher.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, CourseDO> implements CourseService {

    @Autowired
    TeacherService teacherService;

    @Override
    public IPage<CourseListVO> pageCourse(CoursePageQuery pageQuery) {

        LambdaQueryWrapper<CourseDO> queryWrapper = Wrappers.<CourseDO>lambdaQuery()
                .eq(pageQuery.getEnableState() != null, CourseDO::getEnableState, pageQuery.getEnableState())
                .like(CourseDO::getName, pageQuery.getCourseName())
                .orderByAsc(CourseDO::getName);

        IPage<CourseDO> doPage = page(new Page<>(pageQuery.getCurrent(), pageQuery.getSize()), queryWrapper);

        IPage<CourseListVO> voPage = AbstractCourseConverter.INSTANCE.doPage2ListVoPage(doPage);

        for (int i = 0; i < voPage.getRecords().size(); i++) {
            CourseListVO courseListVO = voPage.getRecords().get(i);
            List<Integer> list = (List<Integer>) JSONUtils.parse(courseListVO.getCardType());
            courseListVO.setCardTypeList(list);
        }
        return voPage;
    }

    @Override
    public List<CourseListVO> refList() {

        LambdaQueryWrapper<CourseDO> queryWrapper = Wrappers.<CourseDO>lambdaQuery()
                .eq(CourseDO::getEnableState, EnableStateEnum.ENABLED.getCode())
                .orderByAsc(CourseDO::getName);

        List<CourseDO> doList = list(queryWrapper);

        List<CourseListVO> voList = AbstractCourseConverter.INSTANCE.doList2ListVoList(doList);

        return voList;
    }

    @Override
    public CourseListVO getCourseById(Long id) {

        CourseDO dataObj = getById(id);

        CourseListVO courseListVO = AbstractCourseConverter.INSTANCE.do2ListVO(dataObj);

        List<Boolean> res = new ArrayList<Boolean>();
        List<Integer> list = (List<Integer>) JSONUtils.parse(courseListVO.getCardType());
        courseListVO.setCardTypeList(list);
        return courseListVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCourse(CourseSaveDTO saveDTO) {
        // check
        if (saveDTO.getMaximum() < saveDTO.getMinimum()) {
            throw new BusinessException("【最多可容纳人数】不能小于【最少开课人数】");
        }
        CourseDO dataObj = AbstractCourseConverter.INSTANCE.saveDto2DO(saveDTO);
        dataObj.setCardType(saveDTO.getCardType());

        boolean save = save(dataObj);
        /**
         * 配置表
         */
        teacherService.addCourse(dataObj.getId());

        return save;
    }

    @Override
    public boolean updateCourseById(Long id, CourseSaveDTO saveDTO) {

        CourseDO dataObj = AbstractCourseConverter.INSTANCE.saveDto2DO(saveDTO);
        dataObj.setId(id);
        return updateById(dataObj);
    }

    @Override
    public boolean updateCourseEnableStateById(Long id, Integer enableState) {

        CourseDO dataObj = new CourseDO();
        dataObj.setId(id);
        dataObj.setEnableState(enableState);

        return updateById(dataObj);
    }


}
