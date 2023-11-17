package com.zihe.tams.module.card.controller;

import com.zihe.tams.common.base.BaseController;
import com.zihe.tams.common.exception.BusinessException;
import com.zihe.tams.common.model.ApiResult;
import com.zihe.tams.module.card.component.CardComponent;
import com.zihe.tams.module.card.model.dto.*;
import com.zihe.tams.module.card.model.request.CreateCardRequest;
import com.zihe.tams.module.card.model.request.GetPersonInfoRequest;
import com.zihe.tams.module.card.model.request.RechargeRequest;
import com.zihe.tams.module.card.model.request.RefundRequest;
import com.zihe.tams.module.card.model.response.GetCategoryListResponse;
import com.zihe.tams.module.card.model.response.GetPersonCardInfoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 会员卡 控制器
 *
 * @author liuqisong liukingson@aliyun.com
 * @since 1.0.0 2022-03-15
 */
@RestController
@RequestMapping("vip-card")
@Api(tags = "会员卡 Controller")
public class CardController extends BaseController {

    @Resource
    private CardComponent cardComponent;

    @PostMapping("/create")
    @ApiOperation(value = "会员卡办理接口")
    public ResponseEntity<ApiResult<?>> createCard(@RequestBody CreateCardRequest request) {
        cardComponent.createCard(request);
        return success();
    }

    @ApiOperation(value = "停启用")
    @PutMapping("{id}/enable-state/{enableState}")
    public ResponseEntity<ApiResult<?>> updateCardEnableStateById(@PathVariable("id") Long id, @PathVariable("enableState") Integer enableState) {

        return successOrFail(cardComponent.updateCardEnableStateById(id, enableState));
    }

    @ApiOperation(value = "停启用")
    @PutMapping("{id}/isFree/{isFree}")
    public ResponseEntity<ApiResult<?>> updateCardIsFreeById(@PathVariable("id") Long id, @PathVariable("isFree") Integer isFree) {
        return successOrFail(cardComponent.updateCardIsFreeById(id, isFree));
    }

    @PostMapping("/recharge")
    @ApiOperation(value = "充值接口")
    public ResponseEntity<ApiResult<?>> recharge(@RequestBody RechargeRequest request) {
        cardComponent.recharge(request);
        return success();
    }

    @PostMapping("/refund")
    @ApiOperation(value = "退款接口")
    public ResponseEntity<ApiResult<?>> refund(@RequestBody RefundRequest request) {
        if (request.getPayMethod() == null) {
            throw new BusinessException("请选择出账方式");
        }
        cardComponent.refund(request);
        return success();
    }

    @PostMapping("/categoryList")
    @ApiOperation(value = "分类列表")
    public ResponseEntity<ApiResult<List<GetCategoryListResponse>>> getCategoryList() {
        return success(cardComponent.getCategoryList());
    }

    @PostMapping("/getPersonInfo")
    @ApiOperation(value = "获取使用记录")
    public ResponseEntity<ApiResult<List<GetPersonCardInfoResponse>>> getPersonInfo(@RequestBody GetPersonInfoRequest request) {
        return success(cardComponent.getPersonInfo(request));
    }

    @GetMapping("/computeRefund")
    public ResponseEntity<ApiResult<RefundComputedDTO>> computeRefund(RefundRequest request) {
        if (request.getChargeRate().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new BusinessException("手续费比例不能大于100%");
        }
        if (request.getChargeRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("手续费比例不能小于0");
        }
        return success(cardComponent.computeRefund(request));
    }

    @PostMapping("/config")
    public ResponseEntity<ApiResult<?>> config(@RequestBody List<RechargeCardConfigDTO> cardConfigDTO) {
        for (RechargeCardConfigDTO rechargeCardConfigDTO : cardConfigDTO) {
            for (Integer integer : rechargeCardConfigDTO.getDiscount()) {
                if (!(0 < integer && integer <= 100)) {
                    throw new BusinessException("折扣不能超过100%, 或小于等于0");
                }
            }
        }
        return successOrFail(cardComponent.config(cardConfigDTO));
    }

    @GetMapping("/config")
    public ResponseEntity<ApiResult<List<RechargeCardConfigDTO>>> listConfig() {
        return success(cardComponent.listConfig());
    }

    @PutMapping("changeDate")
    public ResponseEntity<ApiResult<?>> changeDate(@RequestBody CardDateChangeDTO changeDTO) {

        return successOrFail(cardComponent.changeDate(changeDTO));
    }

    @GetMapping("getCardTypes")
    public ResponseEntity<ApiResult<List<CardTypeVO>>> getCardType() {
        return success(cardComponent.getAllCardType());
    }

}
