package com.zihe.tams.module.course.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zihe.tams.common.exception.BusinessException;
import com.zihe.tams.common.util.CollectionUtils;
import com.zihe.tams.common.util.CommonUtil;
import com.zihe.tams.common.util.ExistCheckUtils;
import com.zihe.tams.module.card.constants.BillTypeEnum;
import com.zihe.tams.module.card.dao.CardRuleMapper;
import com.zihe.tams.module.card.model.data.*;
import com.zihe.tams.module.card.model.dto.*;
import com.zihe.tams.module.card.service.CardBillService;
import com.zihe.tams.module.card.service.CardRuleService;
import com.zihe.tams.module.card.service.CardService;
import com.zihe.tams.module.card.service.RechargeService;
import com.zihe.tams.module.course.dao.CourseAppointMapper;
import com.zihe.tams.module.course.dao.CourseMapper;
import com.zihe.tams.module.course.model.convert.AbstractCourseConverter;
import com.zihe.tams.module.course.model.data.*;
import com.zihe.tams.module.course.model.dto.CourseAppointDTO;
import com.zihe.tams.module.course.model.dto.CourseAppointPageQuery;
import com.zihe.tams.module.course.model.dto.CustomerCourseListVO;
import com.zihe.tams.module.course.model.dto.CustomerCoursePageQuery;
import com.zihe.tams.module.course.model.vo.CourseAppointListVO;
import com.zihe.tams.module.course.model.vo.CourseListVO;
import com.zihe.tams.module.course.service.CourseAppointService;
import com.zihe.tams.module.course.service.CourseService;
import com.zihe.tams.module.coursescheduling.dao.CourseSchedulingMapper;
import com.zihe.tams.module.coursescheduling.model.vo.CourseSchedulingListVO;
import com.zihe.tams.module.coursescheduling.service.CourseSchedulingService;
import com.zihe.tams.module.customer.dao.CustomerMapper;
import com.zihe.tams.module.customer.model.data.CustomerDO;
import com.zihe.tams.module.customer.model.vo.CustomerListVO;
import com.zihe.tams.module.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author huangzihe
 * @date 2022/3/15 7:33 下午
 */
@Service
public class CourseAppointServiceImpl extends ServiceImpl<CourseAppointMapper, CourseAppointDO> implements CourseAppointService {

    @Autowired
    CourseAppointMapper courseAppointMapper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CustomerService customerService;

    @Autowired
    ExistCheckUtils existCheckUtils;

    @Autowired
    CourseSchedulingService courseSchedulingService;

    @Autowired
    CourseSchedulingMapper schedulingMapper;

    @Autowired
    CourseService courseService;

    @Autowired
    CardService cardService;

    @Autowired
    CardRuleService cardRuleService;

    @Autowired
    CardBillService cardBillService;

    @Autowired
    CardRuleMapper cardRuleMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    RechargeService rechargeService;



    public void regularCheck(CourseAppointDTO courseAppointDTO) {
        // 常规数据校验
        Long scheduleId = courseAppointDTO.getScheduleId();
//        Long courseId = courseAppointDTO.getCourseId();
        Long customerId = courseAppointDTO.getCustomerId();
        Long appointId = null;
        if (!existCheckUtils.isCustomerExist(customerId)) {
            throw new BusinessException("会员不存在");
        }
        if (!existCheckUtils.isScheduleExist(scheduleId)) {
            throw new BusinessException("排课不存在");
        }
    }

    /**
     * 预约信息分页
     * @param pageQuery
     * @return
     */
    @Override
    public IPage<CourseAppointListVO> pageCourse(CourseAppointPageQuery pageQuery) {


        LambdaQueryWrapper<CourseAppointDO> queryWrapper = Wrappers.<CourseAppointDO>lambdaQuery()
                .eq(CourseAppointDO::getScheduleId, pageQuery.getScheduleId()).orderByAsc(CourseAppointDO::getState)
                .orderByAsc(CourseAppointDO::getAppointTime).eq(CourseAppointDO::getIsDeleted, 'N');

        IPage<CourseAppointDO> doPage = page(new Page<>(pageQuery.getCurrent(), pageQuery.getSize()), queryWrapper);

        IPage<CourseAppointListVO> voPage = AbstractCourseConverter.INSTANCE.doPage2ListAppointVoPage(doPage);
        voPage.getRecords().forEach(record -> {
            CustomerListVO customer = customerService.getCustomerById(record.getCustomerId());
            CardDTO card = cardService.getByCardId(record.getCardId());
            CardRuleDTO rule = cardRuleService.getByRuleId(card.getCardRuleId());
            CourseSchedulingListVO scheduling = courseSchedulingService.getCourseSchedulingById(pageQuery.getScheduleId());
            record.setName(customer.getName());
            record.setCardName(rule.getName());
            record.setPhoneNum(customer.getPhoneNum());
            record.setAttendNum(courseAppointMapper.attendNum(record.getCustomerId(), scheduling.getTeacherId()));
            try {
                record.setAge(CommonUtil.getAge(customer.getBirthday()));
            } catch (Exception e) {
                log.error("未设置年龄报错");
            }
            record.setAim(customer.getAim());
            record.setHasHurt(customer.getHasHurt());
            record.setHasPracticed(customer.getHasPracticed());
            record.setCustomerType(customer.getType());
        });
        return voPage;
    }

    /**
     * 上提候补
     * @param courseAppointDTO
     * @return
     */
    @Override
    public boolean upWait(CourseAppointDTO courseAppointDTO) {
        LambdaQueryWrapper<CourseAppointDO> queryWrapper = Wrappers.<CourseAppointDO>lambdaQuery()
                .eq(CourseAppointDO::getScheduleId, courseAppointDTO.getScheduleId()).eq(CourseAppointDO::getState, 2)
                .orderByAsc(CourseAppointDO::getAppointTime).eq(CourseAppointDO::getIsDeleted, 'N');

        List<CourseAppointDO> courseAppointDOS = this.baseMapper.selectList(queryWrapper);
        Date appointTime = courseAppointDOS.get(0).getAppointTime();
        LocalDateTime minus = DateUtil.toLocalDateTime(appointTime).minus(1, ChronoUnit.MINUTES);
        Date from = Date.from(minus.atZone(ZoneId.systemDefault()).toInstant());
        return this.baseMapper.upWait(courseAppointDTO.getScheduleId(), courseAppointDTO.getCustomerId(), from);
    }

    /**
     * 课程预约
     * @param courseAppointDTO
     * @return
     */
    @Override
    public boolean appoint(CourseAppointDTO courseAppointDTO) {
        // todo 会员卡相关逻辑  1、 会员卡是否可用检查 团课、普拉提、私教。。。  2、 会员卡次数检查   针对不同卡种有不同的限制，如次卡需要检查剩余次数， 充值卡需要检查余额
        // 常规校验
        String isMobile = "";
        if (courseAppointDTO.getEndType() == 1) {
            isMobile = "移动:";
        }
        regularCheck(courseAppointDTO);
        // 会员卡逻辑校验
        CourseSchedulingListVO schedulingDO = courseSchedulingService.getCourseSchedulingById(courseAppointDTO.getScheduleId());
        Long scheduleId = courseAppointDTO.getScheduleId();
        Long courseId = schedulingDO.getCourseId();
        Long customerId = courseAppointDTO.getCustomerId();
        Long appointId = null;
        // 1、 逻辑校验。 是否已存在记录。
        CourseAppointDO existDO = courseAppointMapper.selectByScheduleIdAndCustomerId(scheduleId, customerId);
        if (existDO != null) {
            // 1.1、状态为已约，则前端提示" 您已预约成功"
            if (CourseAppointStateEnum.Appointed.getCode().equals(existDO.getState())) {
                throw new BusinessException(isMobile + "您已预约成功!");
            } else if (CourseAppointStateEnum.Cancel.getCode().equals(existDO.getState())) {
                // 1.2、状态为取消，则重新预约。
                appointId = existDO.getId();
            } else {
                // 1.3、 候补，则前端提示 "您已处于候补队列"
                throw new BusinessException(isMobile + "您已处于候补队列");
            }
        }
        // 卡校验
        cardCheck(schedulingDO, courseAppointDTO);

        // 2、 判断课程容量是否已满
        // 2.1、 获取课程当前已约人数
        int currentAppointSize = courseAppointMapper.getCurrentAppointSize(scheduleId);
        // 2.2、 获取课程最大容量
        CourseDO courseDO = courseMapper.selectById(courseId);
        Integer maximum = courseDO.getMaximum();
        // 3、进行预约
        // 3.1、 未满设置状态为已约
        CourseAppointDO saveDTO = AbstractCourseConverter.INSTANCE.saveDto2DO(courseAppointDTO);
        saveDTO.setCardId(courseAppointDTO.getCardId());
        String status = "";
        if (currentAppointSize < maximum) {
            // 预约状态为已约
            saveDTO.setId(appointId);
            saveDTO.setState(CourseAppointStateEnum.Appointed.getCode());
            saveDTO.setAppointTime(new Date());
            saveDTO.setAttendance(CourseAppointAttendanceEnum.UNSIGNED.getCode());
            saveOrUpdate(saveDTO);
            status = "已约";
        } else {
            // 3.2、 已满设置状态为候补
            // 预约状态为候补
            saveDTO.setState(CourseAppointStateEnum.WAIT.getCode());
            saveDTO.setId(appointId);
            saveDTO.setAppointTime(new Date());
            saveDTO.setAttendance(CourseAppointAttendanceEnum.UNSIGNED.getCode());
            saveOrUpdate(saveDTO);
            status = "候补";
        }
        return true;
    }

    /**
     * 取消预约
     * @param courseAppointDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(CourseAppointDTO courseAppointDTO) {
        CourseSchedulingListVO scheduling = courseSchedulingService.getCourseSchedulingById(courseAppointDTO.getScheduleId());
        CourseListVO course = courseService.getCourseById(scheduling.getCourseId());
        // 开课前3小时禁止取消
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime attendTime = scheduling.getDate().atTime(scheduling.getAttendTime());
        if (courseAppointDTO.getEndType().equals(1) && attendTime.isBefore(now.plusHours(3))) {
            throw new BusinessException("移动:开课前3小时不得取消课程");
        }

        // 常规校验
        regularCheck(courseAppointDTO);
        // 1、 获取取消前的预约状态
        CourseAppointDO oldValue = courseAppointMapper.selectByScheduleIdAndCustomerId(courseAppointDTO.getScheduleId(), courseAppointDTO.getCustomerId());
        Integer state = oldValue.getState();
        if (CourseAppointStateEnum.Cancel.getCode().equals(state)) {
            throw new BusinessException("已经取消预约");
        }
        // 2、 会员预约状态设置为取消
        boolean ok = courseAppointMapper.cancelAppoint(courseAppointDTO.getScheduleId(), courseAppointDTO.getCustomerId());

        // b端在开课前3小时取消，则增加一次取消次数
        if (courseAppointDTO.getEndType().equals(0) && attendTime.isBefore(now.plusHours(3)) &&  now.isBefore(attendTime)) {
            courseAppointMapper.addCancelTime(courseAppointDTO.getCardId());
        }
        if (!ok) {
            throw new BusinessException("预约取消失败, 请联系管理员");
        }
        if (CourseAppointStateEnum.WAIT.getCode().equals(state)) {
            return true;
        }
        // 3、 若原预约状态为已约，则再从候补队列选取最近一名会员，将状态设置为已约
        CourseAppointDO firstWait = courseAppointMapper.selectFirstWait(courseAppointDTO.getScheduleId());
        if (firstWait == null) {
            return true;
        }
        firstWait.setState(CourseAppointStateEnum.Appointed.getCode());
        ok = updateById(firstWait);
        if (!ok) {
            throw new BusinessException("预约取消失败, 请联系管理员");
        }
        return true;
    }


    /**
     * 签到
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean signIn(CourseAppointDTO request) {
        // 1、 修改状态
        boolean ok = courseAppointMapper.signIn(request.getScheduleId(), request.getCustomerId());
        if (!ok) {
            throw new BusinessException("签到失败, 请联系管理员!");
        }
        // 2、 取值
        CardDO cardDO = cardService.getById(request.getCardId());
        CardRuleDO ruleDO = cardRuleService.getById(cardDO.getCardRuleId());
        CourseSchedulingListVO schedulingDO = courseSchedulingService.getCourseSchedulingById(request.getScheduleId());
        CourseListVO courseDO = courseService.getCourseById(schedulingDO.getCourseId());
        CardBillDO cardBillDO = new CardBillDO();

        // 3、 cardDO扣费  cardBill新增
        if (CardTypeEnum.NUMBER.getCode().equals(ruleDO.getType())) {
            cardBillDO.setPreCount(cardDO.getValidityCount());
            cardBillDO.setConsumptionCount(1);
            cardBillDO.setAvailableCount(cardDO.getValidityCount() - 1);
            cardDO.setValidityCount(cardDO.getValidityCount() - 1);
        } else if (CardTypeEnum.TIME.getCode().equals(ruleDO.getType())) {
            // 时长卡也插入一条记录, 证明上了课
        } else if (CardTypeEnum.RECHARGE.getCode().equals(ruleDO.getType())) {
            cardBillDO.setPreBalance(cardDO.getBalance());
            List<Integer> cardTypes = (List<Integer>) JSONUtil.parse(courseDO.getCardType());
            int convertedCardType = -1;
            if (cardTypes.contains(123)) {
                convertedCardType = 0;
            } else if (cardTypes.contains(4)) {
                convertedCardType = 1;
            } else if (cardTypes.contains(5)) {
                convertedCardType = 2;
            } else if (cardTypes.contains(6)) {
                convertedCardType = 3;
            } else if (cardTypes.contains(7)) {
                convertedCardType = 4;
            } else if (cardTypes.contains(10)) {
                convertedCardType = 5;
            } else if (cardTypes.contains(9)) {
                // 大礼包充值卡
                convertedCardType = 6;
            }
            if (convertedCardType == - 1) {
                throw new BusinessException("签到失败，请联系管理员。错误码008");
            }
            BigDecimal coursePrice = null;
            if (ruleDO.getId() == 8) {
                coursePrice = this.rechargeCountPrice(cardDO.getId(), convertedCardType, cardDO.getCardNo(), courseDO.getPrice());

                // 对吴晶的卡进行特殊处理
                if ((cardDO.getId().equals(385L) || cardDO.getId().equals(389L) || cardDO.getId().equals(397L)) && cardTypes.contains(7)) {
                    coursePrice = BigDecimal.valueOf(218);
                }

                if (cardDO.getId().equals(642L)) {
                    if (cardTypes.contains(5) || cardTypes.contains(6)) {
                        coursePrice = BigDecimal.valueOf(550);
                    } else {
                        coursePrice = courseDO.getPrice();
                    }
                }
                // 苏杰孕产卡
                if (cardDO.getId().equals(838L)) {
                    coursePrice = courseDO.getPrice().multiply(BigDecimal.valueOf(0.75));
                }

            } else if (ruleDO.getId() == 15) {
                if (cardDO.getId().equals(838L)) {
                    coursePrice = courseDO.getPrice().multiply(BigDecimal.valueOf(0.75));
                } else {
                    coursePrice = this.rechargeCountPriceForPregnant(0, cardDO.getCardNo(), courseDO.getPrice());
                }
            } else {
                coursePrice = courseDO.getPrice();
            }
            cardBillDO.setConsumptionAmount(coursePrice);
            cardBillDO.setAvailableBalance(cardDO.getBalance().subtract(coursePrice));
            cardDO.setBalance(cardBillDO.getAvailableBalance());
        }
        cardBillDO.setCardNo(cardDO.getCardNo());
        cardBillDO.setCustomerId(request.getCustomerId());
        cardBillDO.setSourceId(request.getScheduleId());
        cardBillDO.setType(BillTypeEnum.COURSE.getType());
        cardService.updateById(cardDO);
        cardBillService.save(cardBillDO);

        return true;
    }

    /**
     * 取消签到
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public boolean cancelSignIn(CourseAppointDTO courseAppointDTO) {
        // 1、 修改状态
        boolean ok = courseAppointMapper.cancelSignIn(courseAppointDTO.getScheduleId(), courseAppointDTO.getCustomerId());
        if (!ok) {
            throw new BusinessException("取消签到失败, 请联系管理员![1]");
        }
        // 2、 回退
        CardBillDTO billDTO = cardBillService.getBillBySourceIdAndCustomerId(courseAppointDTO.getScheduleId(), courseAppointDTO.getCustomerId());
        CardDO card = cardService.getById(courseAppointDTO.getCardId());
        CardRuleDTO ruleDO = cardRuleService.getByRuleId(card.getCardRuleId());
        if (CardTypeEnum.NUMBER.getCode().equals(ruleDO.getType())) {
            card.setValidityCount(card.getValidityCount() + billDTO.getConsumptionCount());
        } else if (CardTypeEnum.TIME.getCode().equals(ruleDO.getType())) {
            // 时长卡直接删一条数据
        } else if (CardTypeEnum.RECHARGE.getCode().equals(ruleDO.getType())) {
            card.setBalance(card.getBalance().add(billDTO.getConsumptionAmount()));
        }
        cardService.updateById(card);
        Boolean isOk = cardBillService.deleteBillLogic(courseAppointDTO.getScheduleId(), courseAppointDTO.getCustomerId());
        if (!isOk) {
            throw new BusinessException("取消签到失败, 请联系管理员![2]");
        }
        isOk = cardBillService.deleteBillLogic(courseAppointDTO.getScheduleId(), courseAppointDTO.getCustomerId());
        if (!isOk) {
            throw new BusinessException("取消签到失败, 请联系管理员![3]");
        }
        return false;
    }

    @Override
    public boolean deleteLogic(Long scheduleId) {
        return this.baseMapper.deleteLogic(scheduleId);
    }


    /**
     * 获取该会员已约课程
     * @param customerId
     * @return
     */
    @Override
    public List<CustomerCourseListVO> getAppointedCourse(Long customerId) {
        return this.baseMapper.getAppointedInfo(customerId);
    }


    /**
     * 会员卡逻辑校验
     * @param courseAppointDTO
     */
    private void cardCheck(CourseSchedulingListVO schedulingDO, CourseAppointDTO courseAppointDTO) {

        // 开课前3小时禁止约课
        LocalDateTime nowTime = LocalDateTimeUtil.now();
        LocalDate date = schedulingDO.getDate();
        LocalDateTime attendTime = date.atTime(schedulingDO.getAttendTime());
        if (attendTime.isBefore(nowTime) && courseAppointDTO.getEndType() == 1) {
            throw new BusinessException("移动:该课程预约已结束");
        }
        if (attendTime.isBefore(nowTime.plusHours(3)) && courseAppointDTO.getEndType() == 1) {
            throw new BusinessException("移动:开课前3小时不得约课");
        }

        String isMobile = "";
        if (courseAppointDTO.getEndType() == 1) {
            isMobile = "移动:";
        }
        // 1、 获取会员卡rule
        Long cardId = courseAppointDTO.getCardId();
        CardDO cardDO = cardService.getById(cardId);
        CardRuleDO ruleDO = cardRuleService.getById(cardDO.getCardRuleId());

        // 2、 获取课程信息
        CourseListVO courseDO = courseService.getCourseById(schedulingDO.getCourseId());
        // 3、 检查
        // 3.1、 时间段检查  1 工作日 2周末
        // 去除团课无限制卡
        if (CardTypeEnum.TIME.getCode().equals(ruleDO.getType()) && ruleDO.getId() != 11) {
            boolean isCardInWeekend = (ruleDO.getPeriod() == 2);
            boolean isCourseInWeekend = false;
//            boolean isCardInWeekend = CardRulePeriodEnum.WEEKEND.getCode().equals(schedulingDO.getDate());
            try {
                isCourseInWeekend = CommonUtil.isWeekend(CommonUtil.localDate2Date(schedulingDO.getDate()));
            } catch (ParseException e) {
                log.error(e.getMessage());
                return;
            }
            // 课程是在周末，但是卡种为工作日卡
            if (isCardInWeekend != isCourseInWeekend && cardId != 707L && cardId != 728L && cardId != 585L && cardId != 766L && cardId != 860L) {
                throw new BusinessException(isMobile + "会员卡上课时间限制！");
            }
        }

        // 3.2 课程类型检查。 卡类型和课程支持的类型对比一下。
        List<Integer> cardType = (List<Integer>) JSONUtils.parse(courseDO.getCardType());
        int ruleId = -1;
        if (ruleDO.getId().intValue() == 1 || ruleDO.getId().intValue() == 2 || ruleDO.getId().intValue() == 3 || ruleDO.getId() == 11 || ruleDO.getId() == 12 || ruleDO.getId() == 14) {
            ruleId = 123;
        } else {
            ruleId = ruleDO.getId().intValue();
        }
        // 体验卡暂时绕过
        if (!cardType.contains(ruleId) && ruleId != 13) {
            throw new BusinessException(isMobile + "该会员卡不在此课程可约范围内");
        }
        // 3.3 余额检查。
        if (CardTypeEnum.NUMBER.getCode().equals(ruleDO.getType())) {
            LocalDateTime endDate = cardDO.getStartTime().plusDays((cardDO.getValidityPeriod()));
            if (endDate.isBefore(LocalDateTime.now())) {
                throw new BusinessException(isMobile + "您的会员卡已过期!");
            }
            // 有效次数 还要减去 已约/候补次数
            int count = courseAppointMapper.countAppointed(cardId);
            if (cardDO.getValidityCount() - count <= 0) {
                throw new BusinessException(isMobile + "可用剩余次数不足!");
            }
        } else if (CardTypeEnum.TIME.getCode().equals(ruleDO.getType())) {
            LocalDateTime endDate = cardDO.getStartTime().plusDays((cardDO.getValidityPeriod()));
            if (endDate.isBefore(LocalDateTime.now())) {
                throw new BusinessException(isMobile + "您的会员卡已过期!");
            }
        } else if (CardTypeEnum.RECHARGE.getCode().equals(ruleDO.getType())) {
            LocalDateTime endDate = cardDO.getStartTime().plusDays((cardDO.getValidityPeriod()));
            if (endDate.isBefore(LocalDateTime.now())) {
                throw new BusinessException(isMobile + "您的会员卡已过期!");
            }
            BigDecimal subtract = cardDO.getBalance().subtract(courseDO.getPrice());
            if (subtract.signum() == -1) {
                throw new BusinessException(isMobile + "可用余额不足");
            }
        } else {

        }
        // 3.4 提前五天预约校验
        // 上课时间
        LocalDate scheduleDate = schedulingDO.getDate();
        // 当前时间
        LocalDate nowDate = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime t21 = LocalTime.of(21, 0);
        LocalTime t21_30 = LocalTime.of(21, 30);

        if (courseAppointDTO.getEndType().equals(EndEnum.C.getCode())) {
            if (now.isAfter(t21_30) && nowDate.plusDays(5 + 1).isAfter(scheduleDate)) {
            } else if (nowDate.plusDays(4 + 1).isAfter(scheduleDate)) {
            } else {
                throw new BusinessException(isMobile + "未到预约时间!");
            }
        }

        // 3.5 工作日或周末预约次数限制
        // 3.5.1 获取本周已约课程
        // 周末和工作日卡无限制时，上面有个约课时间限制也要改一下
        if (EndEnum.C.getCode().equals(courseAppointDTO.getEndType()) && CardTypeEnum.TIME.getCode().equals(ruleDO.getType())) {
            if (cardId.equals(601L) || cardId.equals(707L) || cardId.equals(728L) || cardId.equals(860L)) {
                DateTime monday = DateUtil.beginOfWeek(CommonUtil.localDate2Date(scheduleDate));
                LocalDateTime mondayLdt = LocalDateTimeUtil.of(monday);
                // 上课那周的周六到周日
                DateTime sunday = DateUtil.endOfWeek(CommonUtil.localDate2Date(scheduleDate));
                LocalDateTime sundayLdt = LocalDateTimeUtil.of(sunday);
                int count = courseAppointMapper.getCourseInPeriodCount(courseAppointDTO.getCardId(), monday.toSqlDate(), sunday.toSqlDate());
                Integer frequency = ruleDO.getFrequency();
                if (count + 1 > frequency) {
                    throw new BusinessException(isMobile + "本周可约课次数已满!");
                }
            } else if (cardId.equals(585L) || cardId.equals(766L)){
                // 王小凡  一周两次卡改成3次卡
                DateTime monday = DateUtil.beginOfWeek(CommonUtil.localDate2Date(scheduleDate));
                LocalDateTime mondayLdt = LocalDateTimeUtil.of(monday);
                // 上课那周的周六到周日
                DateTime sunday = DateUtil.endOfWeek(CommonUtil.localDate2Date(scheduleDate));
                LocalDateTime sundayLdt = LocalDateTimeUtil.of(sunday);
                int count = courseAppointMapper.getCourseInPeriodCount(courseAppointDTO.getCardId(), monday.toSqlDate(), sunday.toSqlDate());
                Integer frequency = ruleDO.getFrequency();
                if (count + 1 > 3) {
                    throw new BusinessException(isMobile + "本周可约课次数已满!");
                }
            }
            // 上课那周的周一到周五
            else if (CardRulePeriodEnum.WEEKDAY.getCode().equals(ruleDO.getPeriod())) {
                DateTime monday = DateUtil.beginOfWeek(CommonUtil.localDate2Date(scheduleDate));
                LocalDateTime mondayLdt = LocalDateTimeUtil.of(monday);
                LocalDateTime fridayLdt = mondayLdt.plus(5, ChronoUnit.DAYS).minus(1, ChronoUnit.MILLIS);
                DateTime friday = DateUtil.parseDateTime(LocalDateTimeUtil.format(fridayLdt, "yyyy-MM-dd HH:mm:ss"));

                int count = courseAppointMapper.getCourseInPeriodCount(courseAppointDTO.getCardId(), monday.toSqlDate(), friday.toSqlDate());
                if (count + 1 > ruleDO.getFrequency()) {
                    throw new BusinessException(isMobile + "本周可约课次数已满!");
                }
            } else if (CardRulePeriodEnum.WEEKEND.getCode().equals(ruleDO.getPeriod())) {
                // 上课那周的周六到周日
                DateTime sunday = DateUtil.endOfWeek(CommonUtil.localDate2Date(scheduleDate));
                LocalDateTime sundayLdt = LocalDateTimeUtil.of(sunday);
                LocalDateTime saturdayLDT = sundayLdt.plus(-2, ChronoUnit.DAYS).plus(1, ChronoUnit.SECONDS);
                DateTime saturday = DateUtil.parseDateTime(LocalDateTimeUtil.format(saturdayLDT, "yyyy-MM-dd HH:mm:ss"));

                int count = courseAppointMapper.getCourseInPeriodCount(courseAppointDTO.getCardId(), saturday.toSqlDate(), sunday.toSqlDate());
                if (count + 1 > ruleDO.getFrequency()) {
                    throw new BusinessException(isMobile + "本周可约课次数已满!");
                }
            }
        }
    }




    @Override
    public IPage<CustomerCourseListVO> pageCustomerCourse(CustomerCoursePageQuery pageQuery) {
        List<CustomerCourseListVO> customerCourseListVOS = courseAppointMapper.pageCustomerCourse(pageQuery.getCustomerId(), (pageQuery.getCurrent() - 1) * pageQuery.getSize(), pageQuery.getSize());
        if (customerCourseListVOS != null) {
            customerCourseListVOS.forEach(vo -> {
                String finishTime = vo.getFinishTime().toString();
                vo.setFinishTimeStr(finishTime);
                String attendTime = vo.getAttendTime().toString();
                vo.setAttendTimeStr(attendTime);

            });
        }
        Page<CustomerCourseListVO> page = new Page<>();
        page.setRecords(customerCourseListVOS);
        page.setSize(pageQuery.getSize());
        page.setCurrent(pageQuery.getCurrent());
        int total = courseAppointMapper.getTotal(pageQuery.getCustomerId());
        page.setTotal(total);
        return page;
    }

    /**
     * 实际约课人数
     * @param scheduleId
     * @return
     */
    @Override
    public Integer getAppointNum(Long scheduleId) {
        return courseAppointMapper.getAppointNum(scheduleId);
    }



    /**
     * 签到人数
     * @param scheduleId
     * @return
     */
    @Override
    public Integer getAttendanceNum(Long scheduleId) {
        return courseAppointMapper.getAttendanceNum(scheduleId);
    }

    /**
     * 给充值卡按折扣规则扣钱  [常规团课，vip团课， 常规私教，普拉提私教， 普拉提团课，孕产课]
     */
    public BigDecimal rechargeCountPrice(Long cardId, Integer courseType, String cardNo, BigDecimal price) {
        // 1、 找到充值的金额
        RechargeDTO rechargeDO = rechargeService.getPersonRechargeDO(cardNo);
        if (cardId.equals(397L)) {
            rechargeDO.setPaymentAmount(rechargeDO.getPaymentAmount().add(BigDecimal.valueOf(6000)));
        }
        // 2、 获取规则
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "充值卡");
        List<CardRuleDO> cardRuleDOS = this.cardRuleMapper.selectByMap(map);
        if (CollectionUtils.isEmpty(cardRuleDOS)) {
            throw new BusinessException("服务器配置信息错误,请联系管理员!");
        }
        CardRuleDO cardRuleDO = cardRuleDOS.get(0);

        List<RechargeCardConfigDTO> configList = JSONObject.parseArray(cardRuleDO.getDiscountRate(), RechargeCardConfigDTO.class);
        // 用于方便算档位
        RechargeCardConfigDTO dummy = new RechargeCardConfigDTO();
        dummy.setValue(BigDecimal.valueOf(10000000));
        configList.add(dummy);
        RechargeCardConfigDTO target = null;
        for (int i = 0; i < configList.size(); i++) {
            if (rechargeDO.getPaymentAmount().compareTo(configList.get(i).getValue()) < 0) {
                target = i - 1 < 0 ? null : configList.get(i - 1);
                break;
            }
        }
        // 处理数组为1 或者超过最大的价格的情况
//        if (configList.size() != 0 && target == null) {
//            // 充值金额大于或等于配置折扣的金额
//            if (configList.get(configList.size() - 1).getValue().compareTo(rechargeDO.getPaymentAmount()) <= 0) {
//                target = configList.get(configList.size() - 1);
//            }
//        }
        // 3、 计算 要除以100
        return target == null ? price : price.multiply(BigDecimal.valueOf(target.getDiscount().get(courseType)).divide(BigDecimal.valueOf(100)).setScale(4, RoundingMode.DOWN));
    }

    /**
     * 给孕产卡按折扣规则扣钱
     */
    public BigDecimal rechargeCountPriceForPregnant(Integer courseType, String cardNo, BigDecimal price) {
        // 1、 找到充值的金额
        RechargeDTO rechargeDO = rechargeService.getPersonRechargeDO(cardNo);
        // 2、 获取规则
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "孕产充值卡");
        List<CardRuleDO> cardRuleDOS = this.cardRuleMapper.selectByMap(map);
        if (CollectionUtils.isEmpty(cardRuleDOS)) {
            throw new BusinessException("服务器配置信息错误,请联系管理员!");
        }
        CardRuleDO cardRuleDO = cardRuleDOS.get(0);

        List<RechargeCardConfigDTO> configList = JSONObject.parseArray(cardRuleDO.getDiscountRate(), RechargeCardConfigDTO.class);
        // 用于方便算档位
        RechargeCardConfigDTO dummy = new RechargeCardConfigDTO();
        dummy.setValue(BigDecimal.valueOf(10000000));
        configList.add(dummy);
        RechargeCardConfigDTO target = null;
        for (int i = 0; i < configList.size(); i++) {
            if (rechargeDO.getPaymentAmount().compareTo(configList.get(i).getValue()) < 0) {
                target = i - 1 < 0 ? null : configList.get(i - 1);
                break;
            }
        }
        // 处理数组为1 或者超过最大的价格的情况
//        if (configList.size() != 0 && target == null) {
//            // 充值金额大于或等于配置折扣的金额
//            if (configList.get(configList.size() - 1).getValue().compareTo(rechargeDO.getPaymentAmount()) <= 0) {
//                target = configList.get(configList.size() - 1);
//            }
//        }
        // 3、 计算 要除以100
        return target == null ? price : price.multiply(BigDecimal.valueOf(target.getDiscount().get(courseType)).divide(BigDecimal.valueOf(100)).setScale(4, RoundingMode.DOWN));
    }

}
