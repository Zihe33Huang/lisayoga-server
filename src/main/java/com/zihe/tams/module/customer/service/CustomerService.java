package com.zihe.tams.module.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zihe.tams.module.card.model.vo.CardListVO;
import com.zihe.tams.module.customer.model.dto.CustomerPageQuery;
import com.zihe.tams.module.customer.model.dto.CustomerSaveDTO;
import com.zihe.tams.module.customer.model.dto.CustomerSearchQuery;
import com.zihe.tams.module.customer.model.dto.LoginResponse;
import com.zihe.tams.module.customer.model.vo.CustomerListVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {
    IPage<CustomerListVO> pageCustomer(CustomerPageQuery pageQuery);

    IPage<CardListVO> pageCustomerCard(CustomerSearchQuery pageQuery);

    List<CustomerListVO> refList();

    CustomerListVO getCustomerById(Long id);

    boolean saveCustomer(CustomerSaveDTO saveDTO);

    boolean updateCustomerById(Long id, CustomerSaveDTO saveDTO);

    boolean updateCustomerEnableStateById(Long id, Integer enableState);

    IPage<CustomerListVO> searchCustomer(CustomerSearchQuery pageQuery);

    boolean uploadFile(Long id, List<MultipartFile> file);

    LoginResponse getCustomerByPhone(String phone, String code);

    boolean delete(Long id);
}
