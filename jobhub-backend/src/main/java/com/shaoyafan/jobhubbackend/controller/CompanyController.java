package com.shaoyafan.jobhubbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaoyafan.jobhubbackend.annotation.AuthCheck;
import com.shaoyafan.jobhubbackend.common.BaseResponse;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.Company;
import com.shaoyafan.jobhubbackend.model.dto.company.*;
import com.shaoyafan.jobhubbackend.service.CompanyService;
import com.shaoyafan.jobhubbackend.utils.ResultUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 企业接口
 *
 * @author SYF
 */
@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyController {

    @Resource
    private CompanyService companyService;

    /**
     * 企业注册
     *
     * @param companyAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = 2)
    @ApiOperation("企业注册")
    public BaseResponse<Long> addCompany(@RequestBody CompanyAddRequest companyAddRequest, HttpServletRequest request) {
        if (companyAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = companyService.addCompany(companyAddRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 编辑企业信息
     *
     * @param companyUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = 2)
    @ApiOperation("编辑企业信息")
    public BaseResponse<Boolean> updateCompany(@RequestBody CompanyUpdateRequest companyUpdateRequest) {
        if (companyUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = companyService.updateCompany(companyUpdateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 用户绑定企业
     *
     * @param companyBindRequest
     * @param request
     * @return
     */
    @PostMapping("/bind")
    @AuthCheck(mustRole = 2)
    @ApiOperation("用户绑定企业")
    public BaseResponse<Boolean> bindCompany(@RequestBody CompanyBindRequest companyBindRequest, HttpServletRequest request) {
        if (companyBindRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = companyService.bindCompany(companyBindRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取企业列表
     *
     * @param companyQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = 0)
    @ApiOperation("分页获取企业列表")
    public BaseResponse<Page<Company>> listCompanyByPage(@RequestBody CompanyQueryRequest companyQueryRequest) {
        if (companyQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = companyQueryRequest.getCurrent();
        long size = companyQueryRequest.getPageSize();
        Page<Company> companyPage = companyService.page(new Page<>(current, size),
                companyService.getCompanyQueryWrapper(companyQueryRequest));
        return ResultUtils.success(companyPage);
    }

    /**
     * 企业审核
     *
     * @param companyIdRequest
     * @return
     */
    @PostMapping("/review")
    @AuthCheck(mustRole = 0)
    @ApiOperation("企业审核")
    public BaseResponse<Boolean> reviewCompany(@RequestBody CompanyIdRequest companyIdRequest) {
        if (companyIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = companyIdRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = companyService.reviewCompany(id);
        return ResultUtils.success(result);
    }

    /**
     * 企业冻结
     *
     * @param companyIdRequest
     * @return
     */
    @PostMapping("/freeze")
    @AuthCheck(mustRole = 0)
    @ApiOperation("企业冻结")
    public BaseResponse<Boolean> freezeCompany(@RequestBody CompanyIdRequest companyIdRequest) {
        if (companyIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = companyIdRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = companyService.freezeCompany(id);
        return ResultUtils.success(result);
    }

}