package com.zihe.tams.module.card.component;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.zihe.tams.common.exception.BusinessException;
import com.zihe.tams.common.util.CollectionUtils;
import com.zihe.tams.common.util.ConvertUtils;
import com.zihe.tams.module.card.constants.BillTypeEnum;
import com.zihe.tams.module.card.dao.CardRuleMapper;
import com.zihe.tams.module.card.dao.RechargeMapper;
import com.zihe.tams.module.card.model.data.*;
import com.zihe.tams.module.card.model.dto.*;
import com.zihe.tams.module.card.model.request.CreateCardRequest;
import com.zihe.tams.module.card.model.request.GetPersonInfoRequest;
import com.zihe.tams.module.card.model.request.RechargeRequest;
import com.zihe.tams.module.card.model.request.RefundRequest;
import com.zihe.tams.module.card.model.response.GetCategoryListResponse;
import com.zihe.tams.module.card.model.response.GetPersonCardInfoResponse;
import com.zihe.tams.module.card.service.*;
import com.zihe.tams.module.course.dao.CourseAppointMapper;
import com.zihe.tams.module.course.model.data.CourseDO;
import com.zihe.tams.module.course.service.impl.CourseAppointServiceImpl;
import com.zihe.tams.module.customer.model.vo.CustomerListVO;
import com.zihe.tams.module.customer.service.CustomerService;
import com.zihe.tams.module.report.dto.response.GetRechargeReportResponse;
import com.zihe.tams.module.teacher.model.vo.TeacherListVO;
import com.zihe.tams.module.teacher.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Array;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Component
@Slf4j
public class CardComponent {

    @Resource
    CardService cardService;

    @Resource
    CardRuleService cardRuleService;

    @Resource
    RechargeService rechargeService;

    @Resource
    CustomerService customerService;

    @Resource
    CardBillService cardBillService;

    @Resource
    RefundService refundService;

    @Autowired
    CourseAppointMapper courseAppointMapper;

    @Autowired
    CardRuleMapper cardRuleMapper;

    @Autowired
    RechargeMapper rechargeMapper;

    @Autowired
    CourseAppointServiceImpl courseAppointService;

    @Autowired
    TeacherService teacherService;

    public boolean updateCardEnableStateById(Long id, Integer enableState) {
        CardDO dataObj = new CardDO();
        dataObj.setId(id);
        dataObj.setEnableState(enableState);

        return cardService.updateById(dataObj);
    }


    public boolean updateCardIsFreeById(Long id, Integer isFree) {
        CardDTO cardDTO = cardService.getByCardId(id);
        RechargeDO rechargeDO = this.rechargeMapper.selectOne(new LambdaQueryWrapper<RechargeDO>()
                .eq(RechargeDO::getCardNo, cardDTO.getCardNo()));
        if (isFree == 1) {
            rechargeDO.setIsDeleted('Y');
        } else {
            rechargeDO.setIsDeleted('N');
        }
        return rechargeService.updateById(rechargeDO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createCard(CreateCardRequest request) {
        CustomerListVO customer = customerService.getCustomerById(Long.valueOf(request.getCustomerId()));
        Assert.notNull(customer, "顾客不存在");
        CardRuleDTO cardRule = cardRuleService.getByRuleId(Long.valueOf(request.getCardRuleId()));
        Assert.notNull(cardRule, "卡类别不存在");
        //创建卡
        CardDO cardDO = ConvertUtils.of(request, CardDO.class);
        //生成卡号
        cardDO.setCardNo(IdWorker.getIdStr());
        //规则
        cardDO.setCardRuleId(Long.valueOf(request.getCardRuleId()));
        //用户id
        cardDO.setCustomerId(Long.valueOf(request.getCustomerId()));
        //开始时间
        cardDO.setStartTime(request.getStartTime());
        //创建卡流水
        CardBillDO cardBillDO = new CardBillDO();
        //创建充值记录
        RechargeDO rechargeDO = new RechargeDO();
        if (cardRule.getType().equals(CardTypeEnum.TIME.getCode())) {
            rechargeDO.setValidityPeriod(request.getValidityPeriod());
        } else if (cardRule.getType().equals(CardTypeEnum.NUMBER.getCode())) {
            cardDO.setValidityCount(request.getCardTimes());
            cardBillDO.setPreCount(0);
            cardBillDO.setConsumptionCount(request.getCardTimes());
            cardBillDO.setAvailableCount(request.getCardTimes());
            rechargeDO.setCardTimes(request.getCardTimes());

        } else if (cardRule.getType().equals(CardTypeEnum.RECHARGE.getCode())) {
            cardDO.setBalance(request.getPaymentAmount());
            cardBillDO.setConsumptionAmount(request.getPaymentAmount());
            cardBillDO.setAvailableBalance(request.getPaymentAmount());
        }


        cardService.save(cardDO);

        rechargeDO.setCardNo(cardDO.getCardNo());
        rechargeDO.setCustomerId(customer.getId());
        rechargeDO.setPayMethod(request.getPayMethod());
        rechargeDO.setChannelFee(request.getChannelFee());
        rechargeDO.setActualAmount(request.getActualAmount());
        rechargeDO.setPaymentAmount(request.getPaymentAmount());
        rechargeDO.setValidityPeriod(request.getValidityPeriod());
        // 0为不免费 1为免费
        if (request.getIsFree() != null && request.getIsFree() == 1) {
            rechargeDO.setIsDeleted('Y');
        }
        rechargeService.save(rechargeDO);

        cardBillDO.setCardNo(cardDO.getCardNo());
        cardBillDO.setCustomerId(customer.getId());
        cardBillDO.setSourceId(rechargeDO.getId());
        cardBillDO.setType(BillTypeEnum.RECHARGE.getType());
        cardBillDO.setPreBalance(BigDecimal.ZERO);
        cardBillService.save(cardBillDO);

        //TODO 创建财务流水

    }

    @Transactional(rollbackFor = Exception.class)
    public void recharge(RechargeRequest request) {
        CustomerListVO customer = customerService.getCustomerById(Long.valueOf(request.getCustomerId()));
        Assert.notNull(customer, "顾客不存在");
        CardDTO cardDTO = cardService.getByCardId(Long.valueOf(request.getCardId()));
        Assert.notNull(cardDTO, "此卡不存在");
        CardRuleDTO cardRule = cardRuleService.getByRuleId(Long.valueOf(cardDTO.getCardRuleId()));
        Assert.notNull(cardRule, "卡类别不存在");
        if (cardDTO.getStatus() == 2) {
            throw new BusinessException("此卡已被退卡");
        }
        if (cardRule.getType() != 3) {
            throw new BusinessException("此卡不是充值卡,暂不支持充值");
        }

        CardDO updateCard = new CardDO();
        updateCard.setId(cardDTO.getId());
        updateCard.setBalance(cardDTO.getBalance().add(request.getPaymentAmount()));
        cardService.updateById(updateCard);

        //创建充值记录
        RechargeDO rechargeDO = new RechargeDO();
        rechargeDO.setCardNo(cardDTO.getCardNo());
        rechargeDO.setCustomerId(customer.getId());
        rechargeDO.setPayMethod(request.getPayMethod());
        rechargeDO.setChannelFee(request.getChannelFee());
        rechargeDO.setActualAmount(request.getActualAmount());
        rechargeDO.setPaymentAmount(request.getPaymentAmount());
        rechargeService.save(rechargeDO);

        //查出此卡上次充值的记录
        CardBillDTO preBill = cardBillService.getNowBill(cardDTO.getCardNo());
        if (Objects.isNull(preBill)) {
            throw new BusinessException("此卡流水异常...");
        }
        // 流水
        CardBillDO cardBillDO = new CardBillDO();
        cardBillDO.setCustomerId(cardDTO.getCustomerId());
        cardBillDO.setSourceId(rechargeDO.getId());
        cardBillDO.setCardNo(cardDTO.getCardNo());
        cardBillDO.setType(BillTypeEnum.RECHARGE.getType());
        cardBillDO.setPreBalance(preBill.getAvailableBalance());
        cardBillDO.setConsumptionAmount(request.getPaymentAmount());
        cardBillDO.setAvailableBalance(preBill.getAvailableBalance().add(request.getPaymentAmount()));
        cardBillService.save(cardBillDO);

        //TODO 创建财务流水
    }

    /**
     * 计算退卡价格
     * @param request
     */
    public RefundComputedDTO computeRefund(RefundRequest request) {
        CustomerListVO customer = customerService.getCustomerById(Long.valueOf(request.getCustomerId()));
        Assert.notNull(customer, "顾客不存在");
        CardDTO cardDTO = cardService.getByCardId(Long.valueOf(request.getCardId()));
        Assert.notNull(cardDTO, "此卡不存在");
        CardRuleDTO cardRule = cardRuleService.getByRuleId(Long.valueOf(cardDTO.getCardRuleId()));
        Assert.notNull(cardRule, "卡类别不存在");
        if (cardDTO.getStatus() == 2) {
            throw new BusinessException("此卡已被退卡");
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        // 根据recharge, 计算出总充值金额
        List<GetRechargeReportResponse> allRecharge = rechargeService.getAllRecharge(cardDTO.getCardNo());
        if (allRecharge == null || allRecharge.size() == 0) {
            throw new BusinessException("此卡流水异常");
        }
        for (int i = 0; i < allRecharge.size(); i++) {
            GetRechargeReportResponse recharge = allRecharge.get(i);
            totalAmount = totalAmount.add(recharge.getPaymentAmount());
        }

        RefundComputedDTO refundDTO = new RefundComputedDTO();
        // 退卡剩余  时长卡为天数 次卡为次数 充值卡为余额
        BigDecimal refundCount;
        // 充值总次数 || 天数 || 总金额
        BigDecimal totalCount = BigDecimal.ZERO;
        // 平均价格 = 充值总价格 / 充值总次数
        BigDecimal averagePrice= BigDecimal.ZERO;;
        // 剩余价格 = 平均价格 * 剩余次数/天数
        BigDecimal refundAmount= BigDecimal.ZERO;;
        // 判断卡类型
        if (CardTypeEnum.TIME.getCode().equals(cardRule.getType())) {
            // 时长卡只能充值一次，就是开卡的时候
            GetRechargeReportResponse recharge = allRecharge.get(0);
            totalCount = BigDecimal.valueOf(recharge.getValidityPeriod());
            Duration duration = Duration.between(cardDTO.getStartTime(), LocalDateTime.now());
            int userDay = Math.toIntExact((duration.toDays()));
            refundCount = totalCount.subtract(BigDecimal.valueOf(userDay));
            // 总充值金额 除以 总次数/天数 等于平均价格
            averagePrice = totalAmount.divide(totalCount, RoundingMode.DOWN);
            refundAmount = averagePrice.multiply(refundCount);
        } else if (CardTypeEnum.NUMBER.getCode().equals(cardRule.getType())) {
            // 次卡只能充值一次，就是开卡的时候
            GetRechargeReportResponse recharge = allRecharge.get(0);
            refundCount = BigDecimal.valueOf(cardDTO.getValidityCount());
            totalCount = BigDecimal.valueOf(recharge.getCardTimes());
            // 总充值金额 除以 总次数/天数 等于平均价格
            averagePrice = totalAmount.divide(totalCount, RoundingMode.DOWN);
            refundAmount = averagePrice.multiply(refundCount);

        } else {
            totalCount = totalAmount;
            averagePrice = BigDecimal.ZERO;
            refundCount = cardDTO.getBalance();
            refundAmount = cardDTO.getBalance();
        }
        refundDTO.setRefundAmount(refundAmount);
        refundDTO.setAveragePrice(averagePrice);
        refundDTO.setRefundCount(refundCount);
        refundDTO.setTotalCount(totalCount);
        refundDTO.setServiceCharge(refundAmount.multiply(request.getChargeRate()).divide(BigDecimal.valueOf(100), RoundingMode.DOWN));
        refundDTO.setActualRefundAmount(refundDTO.getRefundAmount().subtract(refundDTO.getServiceCharge()));
        refundDTO.setTotalAmount(totalAmount);
        refundDTO.setRate(request.getChargeRate() );
        refundDTO.setPayMethod(request.getPayMethod());
        return refundDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void refund(RefundRequest request) {
        CustomerListVO customer = customerService.getCustomerById(Long.valueOf(request.getCustomerId()));
        Assert.notNull(customer, "顾客不存在");
        CardDTO cardDTO = cardService.getByCardId(Long.valueOf(request.getCardId()));
        Assert.notNull(cardDTO, "此卡不存在");
        CardRuleDTO cardRule = cardRuleService.getByRuleId(Long.valueOf(cardDTO.getCardRuleId()));
        Assert.notNull(cardRule, "卡类别不存在");
        if (cardDTO.getStatus() == 2) {
            throw new BusinessException("此卡已被退卡");
        }


        //查出此卡上次充值的记录
        CardBillDTO preBill = cardBillService.getNowBill(cardDTO.getCardNo());
        if (Objects.isNull(preBill)) {
            throw new BusinessException("此卡流水异常...");
        }


        RefundComputedDTO computedDTO = computeRefund(request);
        // 1、 设置卡为退卡状态
        CardDO updateCard = new CardDO();
        updateCard.setStatus(2);
        updateCard.setId(cardDTO.getId());

        // 2、 创建退款记录
        RefundDO refundDO = ConvertUtils.of(request, RefundDO.class);
        refundDO.setCardNo(cardDTO.getCardNo());
        refundDO.setCustomerId(Long.valueOf(cardDTO.getCustomerId()));
        refundDO.setAveragePrice(computedDTO.getAveragePrice());
        refundDO.setCreateTime(LocalDateTime.now());
        refundDO.setChargeRate(request.getChargeRate());
        refundDO.setServiceCharge(computedDTO.getServiceCharge());
        refundDO.setRemark(request.getRemark());
        refundDO.setActualRefundAmount(computedDTO.getActualRefundAmount());
        refundDO.setShouldRefundAmount(computedDTO.getRefundAmount());
        refundDO.setRefundCount(computedDTO.getRefundCount().intValue());
        refundDO.setRefundMethod(request.getPayMethod());
        //卡流水
        refundService.save(refundDO);
        CardBillDO cardBillDO = new CardBillDO();
        cardBillDO.setSourceId(refundDO.getId());
        cardBillDO.setCardNo(cardDTO.getCardNo());
        cardBillDO.setCustomerId(customer.getId());
        cardBillDO.setSourceId(refundDO.getId());
        cardBillDO.setType(BillTypeEnum.REFUND.getType());
        if (cardRule.getType() == 1) {

        }
        if (cardRule.getType() == 2) {
//            updateCard.setValidityCount(0);
            cardBillDO.setPreCount(preBill.getAvailableCount());
            cardBillDO.setConsumptionCount(preBill.getAvailableCount());
            cardBillDO.setAvailableCount(0);
        }
        if (cardRule.getType() == 3) {
//            updateCard.setBalance(BigDecimal.ZERO);
            cardBillDO.setPreBalance(preBill.getAvailableBalance());
            cardBillDO.setConsumptionAmount(preBill.getAvailableBalance());
            cardBillDO.setAvailableBalance(BigDecimal.ZERO);
        }
        cardService.updateById(updateCard);


        cardBillService.save(cardBillDO);

        //TODO 创建财务流水
    }


    public List<GetCategoryListResponse> getCategoryList() {
        return ConvertUtils.ofList(cardRuleService.getBaseMapper().selectList(new LambdaQueryWrapper<>()), GetCategoryListResponse.class);
    }

    public List<GetPersonCardInfoResponse> getPersonInfo(GetPersonInfoRequest request) {

        CustomerListVO customer = customerService.getCustomerById(Long.valueOf(request.getCustomerId()));
        Assert.notNull(customer, "顾客不存在");

        List<GetPersonCardInfoResponse> cardInfoResponses = new ArrayList<>();
        //查询所有正常的卡
        List<CardDTO> cardDOList = cardService.getCardListBy(Long.valueOf(request.getCustomerId()), 1);
        if (CollectionUtils.isEmpty(cardDOList)) {
            return cardInfoResponses;
        }

        for (CardDTO cardDTO : cardDOList) {
            GetPersonCardInfoResponse response = new GetPersonCardInfoResponse();
            response.setId(String.valueOf(cardDTO.getId()));
            CardRuleDTO cardRuleDTO = cardRuleService.getByRuleId(cardDTO.getCardRuleId());
            response.setCardName(cardRuleDTO.getName());
            response.setType(cardRuleDTO.getType());
            response.setEnableState(cardDTO.getEnableState());
            List<CardBillDTO> cardBillDTOList = cardBillService.getBillList(cardDTO.getCardNo());
            if (CollectionUtils.isEmpty(cardBillDTOList)) {
                throw new BusinessException("此卡流水异常...");
            }

            RechargeDTO recharge = rechargeService.getPersonRechargeDO(cardDTO.getCardNo());
            if (CardTypeEnum.TIME.getCode().equals(cardRuleDTO.getType())) {
                response.setValidityPeriod(cardDTO.getValidityPeriod());
                //计算两个日期
                Duration duration = Duration.between(cardDTO.getStartTime(), LocalDateTime.now());
                response.setUsePeriod(Math.toIntExact((duration.toDays())));
                response.setAvailablePeriod(cardDTO.getValidityPeriod() - response.getUsePeriod());
//                response.setAvailableCount(Math.toIntExact(cardDTO.getValidityPeriod() - duration.toDays()));
                response.setOpenCardDay(cardDTO.getStartTime().toLocalDate().toString());
                try {
                    response.setEndCardDay(cardDTO.getStartTime().plusDays((cardDTO.getValidityPeriod())).format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw new BusinessException("系统内部错误!");
                }
            }
            // 次卡，为了体现用户使用后减少次数，
            if (CardTypeEnum.NUMBER.getCode().equals(cardRuleDTO.getType())) {
                int count = courseAppointMapper.countAppointed(cardDTO.getId());
                response.setCardTimes(recharge.getCardTimes());
                response.setUseCount(recharge.getCardTimes() - cardBillDTOList.get(0).getAvailableCount() + count);
                response.setAvailableCount(cardBillDTOList.get(0).getAvailableCount() - count);
                // 应要求也加上有效期
                response.setOpenCardDay(cardDTO.getStartTime().toLocalDate().toString());
                try {
                    response.setEndCardDay(cardDTO.getStartTime().plusDays((cardDTO.getValidityPeriod())).format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw new BusinessException("系统内部错误!");
                }
            }
            // 充值卡，也要体现用户使用后减少金额
            if (CardTypeEnum.RECHARGE.getCode().equals(cardRuleDTO.getType())) {
                BigDecimal appointedPrice = BigDecimal.ZERO;
                List<CourseDO> courseDOS = courseAppointMapper.countAppointedForRecharge(cardDTO.getId());
                for (int i = 0; i < courseDOS.size(); i++) {
                    CourseDO courseDO = courseDOS.get(i);
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
                        throw new BusinessException("约课失败，请联系管理员。错误码008");
                    }
                    BigDecimal coursePrice = null;
                    if (cardRuleDTO.getId() == 8) {
                        coursePrice = courseAppointService.rechargeCountPrice(cardDTO.getId(), convertedCardType, cardDTO.getCardNo(), courseDO.getPrice());
                        // 对吴晶的卡进行特殊处理
                        if ((cardDTO.getId().equals(385L) || cardDTO.getId().equals(389L) || cardDTO.getId().equals(397L)) && cardTypes.contains(7)) {
                            coursePrice = BigDecimal.valueOf(218);
                        }
                        // 苏杰孕产卡
                        if (cardDTO.getId().equals(838L)) {
                            coursePrice = courseDO.getPrice().multiply(BigDecimal.valueOf(0.75));
                        }
                    } else if (cardRuleDTO.getId() == 15) {
                        // 苏杰孕产卡
                        if (cardDTO.getId().equals(838L)) {
                            coursePrice = courseDO.getPrice().multiply(BigDecimal.valueOf(0.75));
                        } else {
                            coursePrice = courseAppointService.rechargeCountPriceForPregnant(0, cardDTO.getCardNo(), courseDO.getPrice());
                        }
                    }
                    else {
                        coursePrice = courseDO.getPrice();
                    }
                    appointedPrice = appointedPrice.add(coursePrice);
                }
                response.setUseAmount(recharge.getPaymentAmount().subtract(cardBillDTOList.get(0).getAvailableBalance()).setScale(2, BigDecimal.ROUND_HALF_UP).add(appointedPrice));
                response.setAvailableAmount(cardBillDTOList.get(0).getAvailableBalance().setScale(2, BigDecimal.ROUND_HALF_UP).subtract(appointedPrice));
                // 应要求也加上有效期
                response.setOpenCardDay(cardDTO.getStartTime().toLocalDate().toString());
                try {
                    response.setEndCardDay(cardDTO.getStartTime().plusDays((cardDTO.getValidityPeriod())).format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw new BusinessException("系统内部错误!");
                }
            }

            if (cardRuleDTO.getName().equals("体验卡")) {
                RechargeDO rechargeDO = this.rechargeMapper.selectOne(new LambdaQueryWrapper<RechargeDO>()
                        .eq(RechargeDO::getCardNo, cardDTO.getCardNo()));
                response.setIsFree(rechargeDO.getIsDeleted() == 'N' ? 1 : 2);
            }

            response.setChannelFee(recharge.getChannelFee());
            response.setPayMethod(recharge.getPayMethod());
            response.setPayTimes(recharge.getCardTimes());
            response.setCreateTIme(recharge.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            response.setActualAmount(recharge.getActualAmount());
            response.setPaymentAmount(recharge.getPaymentAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
            response.setCancelTimes(cardDTO.getCancelTimes());
            if (cardDTO.getTeacherId() != null) {
                TeacherListVO teacher = teacherService.getTeacherById(cardDTO.getTeacherId());
                response.setTeacherName(teacher.getName());
            } else {
                response.setTeacherName("/");
            }
            cardInfoResponses.add(response);
        }
        return cardInfoResponses;
    }

    public boolean config(List<RechargeCardConfigDTO> cardConfigDTO) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "充值卡");
        List<CardRuleDO> cardRuleDOS = this.cardRuleMapper.selectByMap(map);
        if (CollectionUtils.isEmpty(cardRuleDOS)) {
            throw new BusinessException("服务器配置信息错误,请联系管理员!");
        }
        CardRuleDO cardRuleDO = cardRuleDOS.get(0);
            cardRuleDO.setDiscountRate(JSONUtil.toJsonStr(cardConfigDTO));

        this.cardRuleMapper.updateById(cardRuleDO);
        return true;
    }

    public List<RechargeCardConfigDTO> listConfig() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "充值卡");
        List<CardRuleDO> cardRuleDOS = this.cardRuleMapper.selectByMap(map);
        if (CollectionUtils.isEmpty(cardRuleDOS)) {
            throw new BusinessException("服务器配置信息错误,请联系管理员!");
        }
        CardRuleDO cardRuleDO = cardRuleDOS.get(0);
        List<RechargeCardConfigDTO> resList = (List<RechargeCardConfigDTO>)JSONUtils.parse(cardRuleDO.getDiscountRate());
        return resList;
    }

    public boolean changeDate(CardDateChangeDTO changeDTO) {
        CardDTO card = cardService.getByCardId(changeDTO.getCardId());
        // 改开卡时间
        Date date = changeDTO.getDate();
        if (changeDTO.getType() == 0) {
            card.setStartTime(LocalDateTimeUtil.of(date));
        } else {
            LocalDateTime startTime = LocalDateTimeUtil.beginOfDay(card.getStartTime());
            LocalDateTime endTime = LocalDateTimeUtil.of(date);
            long between = LocalDateTimeUtil.between(startTime, endTime, ChronoUnit.DAYS);
            card.setValidityPeriod((int)between);
        }
        return cardService.updateById(ConvertUtils.of(card, CardDO.class));
    }

    public boolean punish() {
        // 1、 充值卡， 扣固定的钱，好做

        // 2、 次卡， 扣一次课时费，好做

        // 3、 时长卡，一星期不能约课
        return true;
    }

    public List<CardTypeVO> getAllCardType() {
        ArrayList<CardTypeVO> list = new ArrayList<>();
        for (int type = 1;type <= 3;type++) {
            CardTypeVO cardTypeVO = new CardTypeVO();
            cardTypeVO.setType(type);
            List<CardRuleDTO> cardRuleByType = this.cardRuleService.getCardRuleByType(type);
            cardTypeVO.setList(cardRuleByType);
            list.add(cardTypeVO);
        }
        return list;
    }



}
