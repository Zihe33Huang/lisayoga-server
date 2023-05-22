package com.zihe.tams.module.customer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zihe.tams.common.base.BaseController;
import com.zihe.tams.common.model.ApiResult;
import com.zihe.tams.module.card.model.vo.CardListVO;
import com.zihe.tams.module.course.model.vo.CourseListVO;
import com.zihe.tams.module.customer.model.dto.*;
import com.zihe.tams.module.customer.model.vo.CustomerListVO;
import com.zihe.tams.module.customer.service.CustomerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author huangzihe
 * @date 2022/3/10 8:38 下午
 */
@RestController
@RequestMapping("customer")
public class CustomerController extends BaseController {

    @Autowired
    CustomerService customerService;

    @ApiOperation(value = "分页列表", response = CourseListVO.class)
    @GetMapping
    public ResponseEntity<ApiResult<IPage<CustomerListVO>>> pageCustomer(CustomerPageQuery pageQuery) {

        return success(customerService.pageCustomer(pageQuery));
    }


    @ApiOperation(value = "分页列表", response = CourseListVO.class)
    @GetMapping("search")
    public ResponseEntity<ApiResult<IPage<CustomerListVO>>> searchCustomer(CustomerSearchQuery pageQuery) {

        return success(customerService.searchCustomer(pageQuery));
    }

    @ApiOperation(value = "分页列表", response = CourseListVO.class)
    @GetMapping("customerCard")
    public ResponseEntity<ApiResult<IPage<CardListVO>>> pageCustomerCard(CustomerSearchQuery pageQuery) {
        return success(customerService.pageCustomerCard(pageQuery));
    }


    @ApiOperation(value = "新增")
    @PostMapping
    public ResponseEntity<ApiResult<?>> saveCourse(@Validated @RequestBody CustomerSaveDTO saveDTO) {

        return successOrFail(customerService.saveCustomer(saveDTO));
    }

    @ApiOperation(value = "详情", response = CustomerListVO.class)
    @GetMapping("{id}")
    public ResponseEntity<ApiResult<CustomerListVO>> getCustomerById(@PathVariable("id") Long id) {

        return success(customerService.getCustomerById(id));
    }

    @ApiOperation(value = "修改")
    @PutMapping("{id}")
    public ResponseEntity<ApiResult<?>> updateCustomerById(@PathVariable("id") Long id, @Validated @RequestBody CustomerSaveDTO saveDTO) {

        return successOrFail(customerService.updateCustomerById(id, saveDTO));
    }

    @ApiOperation(value = "是否赠送")
    @PutMapping("{id}/enable-state/{enableState}")
    public ResponseEntity<ApiResult<?>> updateCustomerEnableStateById(@PathVariable("id") Long id, @PathVariable("enableState") Integer enableState) {

        return successOrFail(customerService.updateCustomerEnableStateById(id, enableState));
    }

    @PostMapping("/uploadFile/{id}")
    public ResponseEntity<ApiResult<?>> uploadFile(@PathVariable("id") Long id, @RequestParam("file") List<MultipartFile> file) {
        return successOrFail(customerService.uploadFile(id, file));
    }

    @GetMapping("/login")
    public ResponseEntity<ApiResult<LoginResponse>> getCustomerByPhone(LoginRequest request) {

        return success(customerService.getCustomerByPhone(request.getPhoneNum(), request.getCode()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<?>> delete(@PathVariable("id") Long id) {
        return successOrFail(customerService.delete(id));
    }


}
