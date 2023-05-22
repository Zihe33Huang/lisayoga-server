package com.zihe.tams.module.teacher.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zihe.tams.common.consts.EnableStateEnum;
import com.zihe.tams.common.exception.BusinessException;
import com.zihe.tams.common.util.AliOSSUtil;
import com.zihe.tams.common.util.CommonUtil;
import com.zihe.tams.module.card.dao.CardBillMapper;
import com.zihe.tams.module.card.model.data.TeacherRoyaltyDTO;
import com.zihe.tams.module.course.dao.CourseAppointMapper;
import com.zihe.tams.module.course.dao.CourseMapper;
import com.zihe.tams.module.course.model.data.CourseDO;
import com.zihe.tams.module.coursescheduling.model.dto.CourseSchedulingQuery;
import com.zihe.tams.module.coursescheduling.model.vo.CourseSchedulingListVO;
import com.zihe.tams.module.coursescheduling.service.CourseSchedulingService;
import com.zihe.tams.module.teacher.dao.TeacherCourseFeeMapper;
import com.zihe.tams.module.teacher.dao.TeacherMapper;
import com.zihe.tams.module.teacher.dao.TeacherSalaryMapper;
import com.zihe.tams.module.teacher.model.convert.AbstractTeacherConverter;
import com.zihe.tams.module.teacher.model.data.TeacherCourseFeeDO;
import com.zihe.tams.module.teacher.model.data.TeacherDO;
import com.zihe.tams.module.teacher.model.data.TeacherSalaryDO;
import com.zihe.tams.module.teacher.model.dto.*;
import com.zihe.tams.module.teacher.model.vo.TeacherListVO;
import com.zihe.tams.module.teacher.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, TeacherDO> implements TeacherService {

    public static String prefix = "http://101.133.146.88/lisaPic/";

    @Autowired
    CourseAppointMapper appointMappep;

    @Autowired
    CourseSchedulingService schedulingService;

    @Autowired
    TeacherSalaryMapper salaryMapper;

    @Autowired
    TeacherCourseFeeMapper courseFeeMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CardBillMapper cardBillMapper;

    @Override
    public IPage<TeacherListVO> pageTeacher(TeacherPageQuery pageQuery) {

        LambdaQueryWrapper<TeacherDO> queryWrapper = Wrappers.<TeacherDO>lambdaQuery()
                .eq(pageQuery.getEnableState() != null, TeacherDO::getEnableState, pageQuery.getEnableState())
                .orderByAsc(TeacherDO::getName);

        IPage<TeacherDO> doPage = page(new Page<>(pageQuery.getCurrent(), pageQuery.getSize()), queryWrapper);

        IPage<TeacherListVO> voPage = AbstractTeacherConverter.INSTANCE.doPage2ListVoPage(doPage);
        String thisMonth = DateUtil.now();
        thisMonth = thisMonth.substring(0, thisMonth.lastIndexOf('-'));
        String lastMonth = DateUtil.lastMonth().toString("yyyy-MM");
        for (int i = 0; i < voPage.getRecords().size(); i++) {
            TeacherListVO teacherListVO = voPage.getRecords().get(i);
            try {
                teacherListVO.setAge(CommonUtil.getAge(teacherListVO.getBirthday()));
                LambdaQueryWrapper<TeacherSalaryDO> t = Wrappers.<TeacherSalaryDO>lambdaQuery()
                        .eq(TeacherSalaryDO::getTeacherId, teacherListVO.getId()).eq(TeacherSalaryDO::getMonth, thisMonth);
                List<TeacherSalaryDO> teacherSalaryDO = salaryMapper.selectList(t);
                if (teacherSalaryDO != null && teacherSalaryDO.size() >= 1) {
                    teacherListVO.setIsPaidThisMonth("已结算");
                } else {
                    teacherListVO.setIsPaidThisMonth("未结算");
                }
                LambdaQueryWrapper<TeacherSalaryDO> q = Wrappers.<TeacherSalaryDO>lambdaQuery()
                        .eq(TeacherSalaryDO::getTeacherId, teacherListVO.getId()).eq(TeacherSalaryDO::getMonth, lastMonth);
                List<TeacherSalaryDO> lastMonthDO = salaryMapper.selectList(q);
                if (lastMonthDO != null && lastMonthDO.size() >= 1) {
                    teacherListVO.setIsPaidLastMonth("已结算");
                } else {
                    teacherListVO.setIsPaidLastMonth("未结算");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return voPage;
    }

    @Override
    public List<TeacherListVO> refList() {

        LambdaQueryWrapper<TeacherDO> queryWrapper = Wrappers.<TeacherDO>lambdaQuery()
                .eq(TeacherDO::getEnableState, EnableStateEnum.ENABLED.getCode())
                .orderByAsc(TeacherDO::getName);

        List<TeacherDO> doList = list(queryWrapper);

        List<TeacherListVO> voList = AbstractTeacherConverter.INSTANCE.doList2ListVoList(doList);

        return voList;
    }

    @Override
    public TeacherListVO getTeacherById(Long id) {

        TeacherDO dataObj = getById(id);

        TeacherListVO vo = AbstractTeacherConverter.INSTANCE.do2ListVO(dataObj);
        if (StringUtils.isNotBlank(vo.getPicUrl())) {
            List picList = (List) JSONUtils.parse(vo.getPicUrl());
            vo.setPictureList(picList);
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTeacher(TeacherSaveDTO saveDTO, HttpServletRequest request) {
        // 1、 基础校验
        if (!PhoneUtil.isPhone(saveDTO.getPhoneNum())) {
            throw new BusinessException("手机号码格式错误");
        }

        // 2、 保存
        List<MultipartFile> file = saveDTO.getFile();
        List<String> urlList = new ArrayList<>();
        try {
            for (int i = 0; i < file.size(); i++) {
                String url = AliOSSUtil.upload(file.get(i));
                urlList.add(url);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            throw new BusinessException("文件上传失败!");
        }
        TeacherDO dataObj = AbstractTeacherConverter.INSTANCE.saveDto2DO(saveDTO);
        dataObj.setPicUrl(JSONUtils.toJSONString(urlList));
        save(dataObj);

        /**
         * 配置表
         */
        addTeacher(dataObj.getId());
        return true;
    }

    @Override
    public boolean updateTeacherById(Long id, TeacherUpdateDTO saveDTO) {
        TeacherDO oldTeacherDO = getById(id);
        List<String> oldPicUrl = (List<String>) JSONUtils.parse(oldTeacherDO.getPicUrl());
        List<String> newPictureUrlList = saveDTO.getPictureUrlList();
        Collection<String> intersection = CollectionUtil.intersection(oldPicUrl, newPictureUrlList);
        TeacherDO dataObj = AbstractTeacherConverter.INSTANCE.saveDto2DO(saveDTO);
        dataObj.setId(id);
        List<MultipartFile> file = saveDTO.getFile();
        List<String> urlList = new ArrayList<>();
        try {
            for (int i = 0; i < file.size(); i++) {
                String url = AliOSSUtil.upload(file.get(i));
                urlList.add(url);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            throw new BusinessException("文件上传失败!");
        }
        urlList.addAll(intersection);
        dataObj.setPicUrl(JSONUtils.toJSONString(urlList));
        return updateById(dataObj);
    }

    @Override
    public boolean updateTeacherEnableStateById(Long id, Integer enableState) {

        TeacherDO dataObj = new TeacherDO();
        dataObj.setId(id);
        dataObj.setEnableState(enableState);

        return updateById(dataObj);
    }


    private JSONObject uploadFile(MultipartFile picture, HttpServletRequest request, Long teacherId) {
        System.out.println(picture.getContentType());

        //获取文件在服务器的储存位置
        String path = "/www/lisaPic";
        File filePath = new File(path);
        System.out.println("文件的保存路径：" + path);
        if (!filePath.exists() && !filePath.isDirectory()) {
            System.out.println("目录不存在，创建目录:" + filePath);
            filePath.mkdir();
        }

        //获取原始文件名称(包含格式)
        String originalFileName = picture.getOriginalFilename();
        System.out.println("原始文件名称：" + originalFileName);

        //获取文件类型，以最后一个`.`为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        System.out.println("文件类型：" + type);
        //获取文件名称（不包含格式）
        String name = originalFileName.substring(0, originalFileName.lastIndexOf("."));

        //设置文件新名称: 当前时间+文件名称（不包含格式）
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(d);
        String fileName = date + name + "." + type;
        System.out.println("新文件名称：" + fileName);

        //在指定路径下创建一个文件
        File targetFile = new File(path, fileName);

        //将文件保存到服务器指定位置
        try {
            picture.transferTo(targetFile);
            System.out.println("上传成功");
            List<String> picUrl = new ArrayList<>();
            picUrl.add(prefix + fileName);
            TeacherDO teacherDO = new TeacherDO();
            teacherDO.setId(teacherId);
            teacherDO.setPicUrl(JSONUtils.toJSONString(picUrl));
            updateById(teacherDO);
            //将文件在服务器的存储路径返回
            return null;
        } catch (IOException e) {
            throw new BusinessException("图片保存失败!");
        }
    }


    @Override
    public IPage<TeacherCourseDTO> queryTeacherCourse(TeacherCourseQuery query) {
        List<TeacherCourseDTO> teacherCourseDTOS = appointMappep.queryTeacherCourse(query.getTeacherId(), query.getStartDate(), query.getEndDate(), (query.getCurrent() - 1) * query.getSize(), query.getSize());
        teacherCourseDTOS.forEach(t -> {
            t.setTime(t.getAttendTime().toString() +  " - " + t.getFinishTime().toString());
        });
        Page<TeacherCourseDTO> page = new Page<>();
        page.setRecords(teacherCourseDTOS);
        page.setSize(query.getSize());
        page.setCurrent(query.getCurrent());
        return page;
    }

    /**
     * 月工资结算
     * day是某个月的某天
     */
    @Override
    public TeacherPayDTO countPayment(Long teacherId, Date day, Date startDay, Date endDay, String type) {
        TeacherDO teacherDO = getById(teacherId);
        TeacherPayDTO res = new TeacherPayDTO();
        res.setFeeDTO(new ArrayList<>());
        DateTime begin = null;
        DateTime end = null;
        // 查出这个月的schedule记录， 然后算
        if (type.equals("month")) {
            begin = DateUtil.beginOfMonth(day);
            end = DateUtil.endOfMonth(day);
        } else {
            begin = DateUtil.beginOfDay(startDay);
            end = DateUtil.endOfDay(endDay);
        }
        // 上个月的开始和结束
        String lastMonthBegin = LocalDateTimeUtil.format(LocalDateTimeUtil.of(begin).minus(1, ChronoUnit.MONTHS), "yyyy-MM-dd");
        String lastMonthEnd = LocalDateTimeUtil.format(LocalDateTimeUtil.of(begin).minus(1, ChronoUnit.MILLIS), "yyyy-MM-dd HH:mm:ss");
        CourseSchedulingQuery query = new CourseSchedulingQuery();
        query.setTeacherIdList(Arrays.asList(teacherId));
        if (type.equals("month")) {
            query.setStartDate(begin.toString("yyyy-MM-dd"));
            query.setEndDate(end.toString("yyyy-MM-dd HH:mm:ss"));
        }
        // 不管是day还是Month,都是查出这个月总共的课

        if ("month".equals(type)) {
            List<CourseSchedulingListVO> schedulingListVOS = schedulingService.listCourseScheduling(query);
            List<SchedulingCourseFeeDTO> resList = AbstractTeacherConverter.INSTANCE.do2dtoList3(schedulingListVOS);
            List<TeacherCourseFeeDO> courseFeeList = courseFeeMapper.queryByTeacherId(teacherId);
            for (int i = 0; i < resList.size(); i++) {
                SchedulingCourseFeeDTO r = resList.get(i);
                r.setTime(r.getAttendTime().toString() + " - " + r.getFinishTime().toString());
                if (TeacherTypeEnum.FULLTIME.getCode().equals(teacherDO.getType()) && i < 20) {
                    // 前20节课免费
                    r.setCourseFee(BigDecimal.valueOf(0));
                    continue;
                }
                for (int j = 0; j < courseFeeList.size(); j++) {
                    TeacherCourseFeeDO teacherCourseFeeDO = courseFeeList.get(j);
                    if (teacherCourseFeeDO.getCourseId().equals(r.getCourseId())) {
                        r.setCourseFee(teacherCourseFeeDO.getCourseFee());
                        break;
                    }
                }
            }
            res.setFeeDTO(resList);
        } else {
            // 查出按日期的课程
            DateTime thisDate = begin;
            long monthNum = DateUtil.betweenMonth(startDay, endDay, true) + 1;
            for (int month = 0; month < monthNum; month++) {
                /**
                 * 算的是每个月的总共的
                 */
                DateTime monthBegin = DateUtil.beginOfMonth(thisDate);
                DateTime monthEnd = DateUtil.endOfMonth(thisDate);
                query.setStartDate(monthBegin.toString("yyyy-MM-dd"));
                query.setEndDate(monthEnd.toString("yyyy-MM-dd HH:mm:ss"));
                List<CourseSchedulingListVO> schedulingListVOS = schedulingService.listCourseScheduling(query);
                List<SchedulingCourseFeeDTO> resList = AbstractTeacherConverter.INSTANCE.do2dtoList3(schedulingListVOS);
                List<TeacherCourseFeeDO> courseFeeList = courseFeeMapper.queryByTeacherId(teacherId);

                /**
                 * 算的是时间范围内的
                 */
                CourseSchedulingQuery query2 = new CourseSchedulingQuery();
                query2.setTeacherIdList(Arrays.asList(teacherId));
                query2.setStartDate(monthBegin.isAfter(begin) ? monthBegin.toString("yyyy-MM-dd") :begin.toString("yyyy-MM-dd"));
                query2.setEndDate(monthEnd.isAfter(end) ? end.toString("yyyy-MM-dd HH:mm:ss") : monthEnd.toString("yyyy-MM-dd HH:mm:ss"));
                List<CourseSchedulingListVO> schedulingListVOS1 = schedulingService.listCourseScheduling(query2);
                List<SchedulingCourseFeeDTO> dayResList = AbstractTeacherConverter.INSTANCE.do2dtoList3(schedulingListVOS1);
                // 在这个月全部课中，找到dayResLsit的第一个序号超过20的课
                int index = Integer.MAX_VALUE;
                for (int i = 20; i < resList.size(); i++) {
                    SchedulingCourseFeeDTO r = resList.get(i);
                    for (int j = 0; j < dayResList.size(); j++) {
                        if (r.getId().equals(dayResList.get(j).getId())) {
                            // dayResList中是20节课后的最早的课
                            index = j;
                            break;
                        }
                    }
                    if (index != Integer.MAX_VALUE) {
                        break;
                    }
                }
                // index == -1 说明dayResList中所有的课，都在前20节课中
                for (int i = 0; i < dayResList.size(); i++) {
                    SchedulingCourseFeeDTO r = dayResList.get(i);
                    r.setTime(r.getAttendTime().toString() + " - " + r.getFinishTime().toString());
                    if (i >= index) {
                        for (int j = 0; j < courseFeeList.size(); j++) {
                            TeacherCourseFeeDO teacherCourseFeeDO = courseFeeList.get(j);
                            if (teacherCourseFeeDO.getCourseId().equals(r.getCourseId())) {
                                r.setCourseFee(teacherCourseFeeDO.getCourseFee());
                                break;
                            }
                        }
                    } else {
                        r.setCourseFee(BigDecimal.valueOf(0));
                    }
                }
                // 去下个月
                thisDate = DateUtil.parse(LocalDateTimeUtil.of(thisDate).plus(1, ChronoUnit.MONTHS).format(DateTimeFormatter.ISO_DATE), "yyyy-MM-dd");
                res.getFeeDTO().addAll(dayResList);
            }

        }



        if (type.equals("month")) {
            /**
             * 如果已结算，则从表中查询，而不是计算
             */
            String month = DateUtil.format(day, "yyyy-MM");
            HashMap<String, Object> map = new HashMap<>();
            map.put("teacher_id", teacherId);
            map.put("month", month);
            map.put("type", 0); // 0为按月结算
            List<TeacherSalaryDO> teacherSalaryDOS = salaryMapper.selectByMap(map);
            if (teacherSalaryDOS != null && !teacherSalaryDOS.isEmpty()) {
                TeacherSalaryDO teacherSalaryDO = teacherSalaryDOS.get(0);
                TeacherPayDTO teacherPayDTO = AbstractTeacherConverter.INSTANCE.do2dto(teacherSalaryDO);
                teacherPayDTO.setStatus(1);
                teacherPayDTO.setFeeDTO(res.getFeeDTO());
                return teacherPayDTO;
            }
        }


        // 当节课时费

        int totalCourse = res.getFeeDTO().size();
        BigDecimal salary = BigDecimal.ZERO;
        if (TeacherTypeEnum.PARTTIME.getCode().equals(teacherDO.getType())) {
            BigDecimal courseFee = BigDecimal.ZERO;
            for (int i = 0; i < res.getFeeDTO().size(); i++) {
                courseFee = courseFee.add(res.getFeeDTO().get(i).getCourseFee());
            }
            res.setCourseFee(courseFee);
            salary = salary.add(courseFee);
        } else if (TeacherTypeEnum.FULLTIME.getCode().equals(teacherDO.getType())){

            // 1、 底薪
            salary = salary.add(teacherDO.getBasicSalary());
            res.setBasicSalary(teacherDO.getBasicSalary());
            // 2、 计算除每月免费上20节课外的单节课时费
            BigDecimal courseFee = BigDecimal.ZERO;
            for (int i = 0; i < totalCourse; i++) {
                courseFee = courseFee.add(res.getFeeDTO().get(i).getCourseFee());
            }
            salary = salary.add(courseFee);
            res.setCourseFee(courseFee);
            // 3、 饭贴车贴
            BigDecimal allowance = teacherDO.getAllowance();
            salary = salary.add(allowance);
            res.setAllowance(allowance);

//            // 4、 开单提成【查询这个月的开卡记录】
//            BigDecimal royalty = BigDecimal.ZERO;
//            List<TeacherRoyaltyDTO> teacherRoyaltyDTOS = cardBillMapper.selectCardByTeacherId(teacherId, begin.toString("yyyy-MM-dd"), end.toString("yyyy-MM-dd HH:mm:ss"));
//            for (int i = 0; i < teacherRoyaltyDTOS.size(); i++) {
//                TeacherRoyaltyDTO t = teacherRoyaltyDTOS.get(i);
//                SchedulingCourseFeeDTO feeDTO = new SchedulingCourseFeeDTO();
//                BigDecimal r = t.getPaymentAmount().multiply(BigDecimal.valueOf(0.04));
//                feeDTO.setCourseFee(r);
//                feeDTO.setDate(t.getDate());
//                feeDTO.setTime("开单提成");
//                feeDTO.setCourseName(t.getCustomerName() + "  " + t.getCardType());
//                resList.add(feeDTO);
//                royalty = royalty.add(r);
//            }
//            salary.add(royalty);
//            res.setRoyalty(royalty);

        }
        // 4、 开单提成【查询这个月的开卡记录】
            BigDecimal royalty = BigDecimal.ZERO;
            List<TeacherRoyaltyDTO> teacherRoyaltyDTOS = cardBillMapper.selectRechargeByTeacherId(teacherId, begin.toString("yyyy-MM-dd"), end.toString("yyyy-MM-dd HH:mm:ss"));
            for (int i = 0; i < teacherRoyaltyDTOS.size(); i++) {
                TeacherRoyaltyDTO t = teacherRoyaltyDTOS.get(i);
                if (t.getCardType().equals("体验卡")) {
                    continue;
                }
                SchedulingCourseFeeDTO feeDTO = new SchedulingCourseFeeDTO();
                BigDecimal r = t.getPaymentAmount().multiply(t.getRoyaltyRate()).divide(BigDecimal.valueOf(100).setScale(4, RoundingMode.DOWN));
                feeDTO.setCourseFee(r);
                feeDTO.setDate(t.getDate());
                feeDTO.setTime("开单提成");
                feeDTO.setCourseName(t.getCustomerName() + "  " + t.getCardType());
                res.getFeeDTO().add(feeDTO);
                royalty = royalty.add(r);
            }

        // 5、 退款扣钱 【查询上个月的退卡记录】
        if ("month".equals(type)) {
            List<TeacherRoyaltyDTO> refundList = cardBillMapper.selectRefundByTeacherId(teacherId, lastMonthBegin, lastMonthEnd);
            for (int i = 0; i < refundList.size(); i++) {
                TeacherRoyaltyDTO t = refundList.get(i);
                if (t.getCardType().equals("体验卡")) {
                    continue;
                }
                SchedulingCourseFeeDTO feeDTO = new SchedulingCourseFeeDTO();
                BigDecimal r = t.getPaymentAmount().multiply(t.getRoyaltyRate()).divide(BigDecimal.valueOf(100).setScale(4, RoundingMode.DOWN));
                feeDTO.setCourseFee(r);
                feeDTO.setDate(t.getDate());
                feeDTO.setTime("退卡扣除");
                feeDTO.setCourseName(t.getCustomerName() + "  " + t.getCardType());
                res.getFeeDTO().add(feeDTO);
                royalty = royalty.add(r);
            }
        }


        salary = salary.add(royalty);
        res.setRoyaltyRate(teacherDO.getRoyaltyRate());
        res.setRoyalty(royalty);
        res.setTotalSalary(salary);
        res.setCount(totalCourse);
        res.setStatus(0);
        return res;
    }

    @Override
    public boolean payConfirm(TeacherPayConfirmDTO confirmDTO) {
        TeacherSalaryDO teacherSalaryDO = AbstractTeacherConverter.INSTANCE.dto2do(confirmDTO);
        String month = confirmDTO.getMonth();
        String substring = month.substring(0, month.lastIndexOf('-'));
        teacherSalaryDO.setMonth(substring);
        teacherSalaryDO.setCourseFee(confirmDTO.getCourseFee());
        teacherSalaryDO.setRoyaltyRate(confirmDTO.getRoyaltyRate());
        teacherSalaryDO.setType(confirmDTO.getType());
        try {
            int insert = salaryMapper.insert(teacherSalaryDO);
            if (insert > 0) {
                return true;
            }
        } catch (Exception e) {
            throw new BusinessException("您已结算!");
        }
        return false;
    }

    @Override
    public List<TeacherPayDTO> listTeacherPay(String startDate, String endDate) {
        startDate = startDate.substring(0, startDate.lastIndexOf('-'));
        endDate = endDate.substring(0, endDate.lastIndexOf('-'));
        LambdaQueryWrapper<TeacherSalaryDO> t = Wrappers.<TeacherSalaryDO>lambdaQuery()
                .in(TeacherSalaryDO::getMonth, startDate, endDate).orderByDesc(TeacherSalaryDO::getMonth);
        List<TeacherSalaryDO> teacherSalaryDOS = salaryMapper.selectList(t);
        List<TeacherPayDTO> teacherPayDTOS = AbstractTeacherConverter.INSTANCE.do2dtoList(teacherSalaryDOS);
        for (int i = 0; i < teacherPayDTOS.size(); i++) {
            teacherPayDTOS.get(i).setPayType(teacherSalaryDOS.get(i).getType());
        }
        teacherPayDTOS.forEach(teacher -> {
            TeacherDO teacherDO = getById(teacher.getTeacherId());
            teacher.setTeacherName(teacherDO.getName());
            teacher.setType(teacherDO.getType());
        });
        return teacherPayDTOS;
    }

    /**
     * 获取该月的老师总工资数
     * @param day
     * @return
     */
    public BigDecimal getALlTeacherSalary(Date day) {
        BigDecimal total = BigDecimal.ZERO;
        String month = DateUtil.format(day, "yyyy-MM");
        HashMap<String, Object> map = new HashMap<>();
        map.put("month", month);
        List<TeacherSalaryDO> teacherSalaryDOS = salaryMapper.selectByMap(map);
        for (int i = 0; i < teacherSalaryDOS.size(); i++) {
            total = total.add(teacherSalaryDOS.get(i).getTotalSalary());
        }
        return total;
    }


    /**
     * 保存配置
     * @return
     */
    @Override
    public boolean saveTeacherCourseFee(List<TeacherCourseFeeDO> list) {
        try {
            list.forEach(obj -> {
                courseFeeMapper.updateById(obj);
            });
        } catch (Exception e) {
            throw new BusinessException("保存配置失败");
        }
        return true;
    }

    /**
     * 列出该老师名下的所有课时费
     * @param teacherId
     * @return
     */
    @Override
    public IPage<TeacherCourseFeeDTO> listTeacherCourseFee(TeacherConfigQuery query) {

        List<TeacherCourseFeeDO> data = courseFeeMapper.queryTeacherConfig(query.getTeacherId(), (query.getCurrent() - 1) * query.getSize(), query.getSize(), query.getName());
        List<TeacherCourseFeeDTO> res = AbstractTeacherConverter.INSTANCE.do2dtoList2(data);
        res.forEach(obj -> {
            CourseDO courseDO = courseMapper.selectById(obj.getCourseId());
            obj.setCourseName(courseDO.getName());
        });
        Page<TeacherCourseFeeDTO> page = new Page<>();
        page.setRecords(res);
        page.setSize(query.getSize());
        page.setCurrent(query.getCurrent());
        page.setTotal(courseFeeMapper.getTotal(query.getTeacherId(), query.getName()));

        return page;
    }

    /**
     *  增加课程时，增加课时费配置
     */
    @Override
    public void addCourse(Long courseId) {
        List<Long> allTeacher = this.baseMapper.getAllTeacher();
        for (int i = 0; i < allTeacher.size(); i++) {
            TeacherCourseFeeDO teacherCourseFeeDO = new TeacherCourseFeeDO();
            teacherCourseFeeDO.setCourseFee(BigDecimal.ZERO);
            teacherCourseFeeDO.setCourseId(courseId);
            teacherCourseFeeDO.setTeacherId(allTeacher.get(i));
            courseFeeMapper.insert(teacherCourseFeeDO);
        }
    }

    /**
     * 增加老师时，增加课时费配置
     * @param teacherId
     */
    @Override
    public void addTeacher(Long teacherId) {
        List<Long> allCourse = courseMapper.getAllCourse();
        allCourse.forEach(c -> {
            TeacherCourseFeeDO teacherCourseFeeDO = new TeacherCourseFeeDO();
            teacherCourseFeeDO.setCourseFee(BigDecimal.ZERO);
            teacherCourseFeeDO.setCourseId(c);
            teacherCourseFeeDO.setTeacherId(teacherId);
            courseFeeMapper.insert(teacherCourseFeeDO);
        });
    }


}
