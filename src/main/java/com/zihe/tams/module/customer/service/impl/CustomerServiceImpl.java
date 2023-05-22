package com.zihe.tams.module.customer.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.PhoneUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zihe.tams.common.exception.BusinessException;
import com.zihe.tams.common.util.AliOSSUtil;
import com.zihe.tams.common.util.CollectionUtils;
import com.zihe.tams.common.util.CommonUtil;
import com.zihe.tams.module.card.component.CardComponent;
import com.zihe.tams.module.card.model.request.GetPersonInfoRequest;
import com.zihe.tams.module.card.model.response.GetPersonCardInfoResponse;
import com.zihe.tams.module.card.model.vo.CardListVO;
import com.zihe.tams.module.coursescheduling.model.vo.CourseSchedulingReportVO;
import com.zihe.tams.module.coursescheduling.service.CourseSchedulingService;
import com.zihe.tams.module.customer.convert.AbstractCustomerConverter;
import com.zihe.tams.module.customer.dao.CustomerMapper;
import com.zihe.tams.module.customer.model.data.CustomerDO;
import com.zihe.tams.module.customer.model.dto.CustomerPageQuery;
import com.zihe.tams.module.customer.model.dto.CustomerSaveDTO;
import com.zihe.tams.module.customer.model.dto.CustomerSearchQuery;
import com.zihe.tams.module.customer.model.dto.LoginResponse;
import com.zihe.tams.module.customer.model.vo.CustomerListVO;
import com.zihe.tams.module.customer.service.CustomerService;
import com.zihe.tams.module.teacher.dao.TeacherMapper;
import com.zihe.tams.module.teacher.model.data.TeacherDO;
import com.zihe.tams.module.uploadfile.dao.UploadFileMapper;
import com.zihe.tams.module.uploadfile.data.UploadFileDO;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author huangzihe
 * @date 2022/3/10 10:42 下午
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, CustomerDO> implements CustomerService {

    @Autowired
    UploadFileMapper uploadFileMapper;

    @Autowired
    CardComponent cardComponent;

    @Autowired
    TeacherMapper teacherMapper;

    @Autowired
    CourseSchedulingService scheduledService;

    @Override
    public IPage<CustomerListVO> pageCustomer(CustomerPageQuery pageQuery) {
        LambdaQueryWrapper<CustomerDO> queryWrapper = Wrappers.<CustomerDO>lambdaQuery()
                .eq(pageQuery.getEnableState() != null, CustomerDO::getEnableState, pageQuery.getEnableState())
                .eq(CustomerDO::getIsDeleted, 'N')
                .orderByDesc(CustomerDO::getEnterTime);

        IPage<CustomerDO> doPage = page(new Page<>(pageQuery.getCurrent(), pageQuery.getSize()), queryWrapper);

        IPage<CustomerListVO> voPage = AbstractCustomerConverter.INSTANCE.doPage2ListVoPage(doPage);
        for (int i = 0; i < voPage.getRecords().size(); i++) {
            CustomerListVO customerListVO = voPage.getRecords().get(i);
            try {
                customerListVO.setAge(CommonUtil.getAge(customerListVO.getBirthday()));
                // 1正常 2 已退卡
                GetPersonInfoRequest req = new GetPersonInfoRequest();
                req.setCustomerId(customerListVO.getId().toString());
                List<GetPersonCardInfoResponse> cards = cardComponent.getPersonInfo(req);
                customerListVO.setCardList(cards);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return voPage;
    }

    @Override
    public IPage<CardListVO> pageCustomerCard(CustomerSearchQuery pageQuery) {
        LambdaQueryWrapper<CustomerDO> queryWrapper = Wrappers.<CustomerDO>lambdaQuery()
                .eq(CustomerDO::getEnableState, 1)
                .like(pageQuery.getName() != null, CustomerDO::getName, pageQuery.getName())
                .eq(CustomerDO::getIsDeleted, 'N')
                .orderByDesc(CustomerDO::getEnterTime);

        IPage<CustomerDO> doPage = page(new Page<>(pageQuery.getCurrent(), pageQuery.getSize()), queryWrapper);

        IPage<CustomerListVO> voPage = AbstractCustomerConverter.INSTANCE.doPage2ListVoPage(doPage);

        List<CardListVO> res = new ArrayList<>();
        for (int i = 0; i < voPage.getRecords().size(); i++) {
            CustomerListVO customerListVO = voPage.getRecords().get(i);
            try {
                customerListVO.setAge(CommonUtil.getAge(customerListVO.getBirthday()));
                // 1正常 2 已退卡
                GetPersonInfoRequest req = new GetPersonInfoRequest();
                req.setCustomerId(customerListVO.getId().toString());
                List<GetPersonCardInfoResponse> cards = cardComponent.getPersonInfo(req);
                if (cards == null) {
                    continue;
                }
                for (GetPersonCardInfoResponse card : cards) {
                    CardListVO cardListVO = new CardListVO();
                    cardListVO.setAvailableAmount(card.getAvailableAmount());
                    cardListVO.setAvailableCount(card.getAvailableCount());
                    cardListVO.setAvailablePeriod(card.getAvailablePeriod());
                    cardListVO.setCardId(card.getId());
                    cardListVO.setCustomerId(customerListVO.getId().toString());
                    cardListVO.setEndCardDay(card.getEndCardDay());
                    cardListVO.setOpenCardDay(card.getOpenCardDay());
                    cardListVO.setCardName(card.getCardName());
                    cardListVO.setCustomerName(customerListVO.getName());
                    cardListVO.setType(card.getType());
                    res.add(cardListVO);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Page<CardListVO> cardListVOPage = new Page<>(voPage.getCurrent(), voPage.getSize(), res.size());
        cardListVOPage.setRecords(res);
        cardListVOPage.setTotal(voPage.getTotal());
        return cardListVOPage;
    }


    @Override
    public List<CustomerListVO> refList() {
        return null;
    }

    @Override
    public CustomerListVO getCustomerById(Long id) {

        CustomerDO dataObj = getById(id);

        CustomerListVO vo = AbstractCustomerConverter.INSTANCE.do2ListVO(dataObj);
        if (StringUtils.isNotBlank(dataObj.getPicUrl())) {
            vo.setFileList((List<String>) JSONUtils.parse(dataObj.getPicUrl()));
        }
        return vo;
    }

    @Override
    public boolean saveCustomer(CustomerSaveDTO saveDTO) {
        CustomerDO dataObj = AbstractCustomerConverter.INSTANCE.saveDto2DO(saveDTO);
        if (!PhoneUtil.isMobile(saveDTO.getPhoneNum())) {
            throw new BusinessException("手机号码格式不正确!");
        }
        if (saveDTO.getType() != 1 && saveDTO.getEnterTime() == null) {
            throw new BusinessException("请输入入会时间!");
        }
        if (saveDTO.getBirthday() == null) {
            throw new BusinessException("请输入出生日期!");
        }
        try {
            save(dataObj);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("该手机号已被注册");
        }
        // 如果是体验会员，则自动给他办一张次卡，叫会员体验卡
//        if (saveDTO.getType() == 1) {
//            CreateCardRequest req = new CreateCardRequest();
//            req.setCustomerId(dataObj.getId().toString());
//            req.setStartTime(LocalDateTime.now());
//            req.setCardRuleId(String.valueOf(13));
//            req.setCardTimes(1);
//            // 大众点评团购
//            req.setPayMethod(String.valueOf(3));
//            // 渠道费用，暂时设置为0
//            req.setChannelFee(BigDecimal.valueOf(5.99));
//            // 实付金额
//            req.setActualAmount(BigDecimal.valueOf(59.9));
//            req.setPaymentAmount(BigDecimal.valueOf(53.91));
//            req.setValidityPeriod(365);
//            cardComponent.createCard(req);
//        }
        return true;
    }

    @Override
    public boolean updateCustomerById(Long id, CustomerSaveDTO saveDTO) {
        CustomerDO dataObj = AbstractCustomerConverter.INSTANCE.saveDto2DO(saveDTO);
        dataObj.setId(id);

        return updateById(dataObj);
    }

    @Override
    public boolean updateCustomerEnableStateById(Long id, Integer enableState) {
        CustomerDO dataObj = new CustomerDO();
        dataObj.setId(id);
        dataObj.setEnableState(enableState);

        return updateById(dataObj);
    }

    @Override
    public IPage<CustomerListVO> searchCustomer(CustomerSearchQuery pageQuery) {
        LambdaQueryWrapper<CustomerDO> queryWrapper = Wrappers.<CustomerDO>lambdaQuery()
                .eq(pageQuery.getEnableState() != null, CustomerDO::getEnableState, pageQuery.getEnableState())
                .like(CustomerDO::getName, pageQuery.getName()).eq(CustomerDO::getIsDeleted, 'N')
                .orderByDesc(CustomerDO::getEnterTime);

        IPage<CustomerDO> doPage = page(new Page<>(pageQuery.getCurrent(), pageQuery.getSize()), queryWrapper);

        IPage<CustomerListVO> voPage = AbstractCustomerConverter.INSTANCE.doPage2ListVoPage(doPage);
        for (int i = 0; i < voPage.getRecords().size(); i++) {
            CustomerListVO customerListVO = voPage.getRecords().get(i);
            try {
                customerListVO.setAge(CommonUtil.getAge(customerListVO.getBirthday()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uploadFile(Long id, List<MultipartFile> file) {
        List<String> urlList = new ArrayList<>();
        for (int i = 0; i < file.size(); i++) {
            try {
                String url = AliOSSUtil.upload(file.get(i));
                UploadFileDO uploadFileDO = new UploadFileDO();
                uploadFileDO.setAssociateId(id);
                uploadFileDO.setFileName(file.get(i).getOriginalFilename());
                uploadFileDO.setType(1);
                uploadFileDO.setUrl(url);
                uploadFileMapper.insert(uploadFileDO);
            } catch (IOException ioException) {
                log.error(ioException.getMessage());
                throw new BusinessException("文件上传失败");
            }
        }
        CustomerDO customerDO = getById(id);
        List<String> picList = null;
        if (StringUtils.isNotBlank(customerDO.getPicUrl())) {
            picList = (List<String>) JSONUtils.parse(customerDO.getPicUrl());
            picList.addAll(urlList);
        } else {
            picList = urlList;
        }
        customerDO.setPicUrl(JSONUtils.toJSONString(picList));
        updateById(customerDO);
        return true;
    }

    @Override
    public LoginResponse getCustomerByPhone(String phone, String code) {
        LoginResponse loginResponse = new LoginResponse();

        HashMap<String, Object> map = new HashMap<>();
        map.put("phone_num", phone);
        map.put("is_deleted", 'N');
        List<CustomerDO> customerDOS = this.baseMapper.selectByMap(map);
        map.remove("is_deleted");
        List<TeacherDO> teacherDOS = teacherMapper.selectByMap(map);
        if (CollectionUtils.isEmpty(customerDOS) && CollectionUtils.isEmpty(teacherDOS)) {
            throw new BusinessException("移动:账户不存在");
        }
        if (!CollectionUtils.isEmpty(customerDOS)) {
            CustomerDO customerDO = null;
            if (customerDOS.size() > 1) {
                for (CustomerDO c : customerDOS) {
                    Integer type = c.getType();
                    if (type.equals(0) || type.equals(2)) {
                        customerDO = c;
                        break;
                    }
                }
            } else {
                customerDO = customerDOS.get(0);
            }
//         若用户存在，则把openId存进去
//          获取openId, 将openId存给对应用户
            try {
                loginResponse.setName(customerDO.getName());
                loginResponse.setType(0);
                loginResponse.setId(customerDO.getId());
                this.baseMapper.updateById(customerDO);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            TeacherDO teacherDO = teacherDOS.get(0);
            DateTime begin = DateUtil.beginOfMonth(new Date());
            DateTime end = DateUtil.endOfMonth(new Date());
            CourseSchedulingReportVO reportOneTeacherCount = scheduledService.getReportOneTeacherCount(teacherDO.getId(), DateUtil.format(begin, "yyyy-MM-dd"), DateUtil.format(end, "yyyy-MM-dd HH:mm:ss"));
//        若用户存在，则把openId存进去
//        获取openId, 将openId存给对应用户
            loginResponse.setName(teacherDO.getName());
            loginResponse.setAkaName(teacherDO.getAkaName());
            loginResponse.setType(1);
            loginResponse.setId(teacherDO.getId());
            loginResponse.setTotalCourse(reportOneTeacherCount.getCount());
        }
        return loginResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        // 删除会员
        CustomerDO customerDO = this.baseMapper.selectById(id);
        customerDO.setIsDeleted('Y');
        this.baseMapper.updateById(customerDO);
        // 删除会员的约课信息
        // 删除会员的预约信息
        // 删除会员的所有费用
        //
        return true;

    }
}
