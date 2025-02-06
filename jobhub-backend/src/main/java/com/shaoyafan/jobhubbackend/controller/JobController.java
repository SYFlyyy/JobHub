package com.shaoyafan.jobhubbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaoyafan.jobhubbackend.annotation.AuthCheck;
import com.shaoyafan.jobhubbackend.common.BaseResponse;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.StatusConstant;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.Job;
import com.shaoyafan.jobhubbackend.model.domain.User;
import com.shaoyafan.jobhubbackend.model.dto.job.JobAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.job.JobQueryRequest;
import com.shaoyafan.jobhubbackend.model.dto.job.JobUpdateRequest;
import com.shaoyafan.jobhubbackend.model.enums.UserRoleEnum;
import com.shaoyafan.jobhubbackend.service.JobCollectionService;
import com.shaoyafan.jobhubbackend.service.JobService;
import com.shaoyafan.jobhubbackend.service.UserService;
import com.shaoyafan.jobhubbackend.utils.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 职位接口
 *
 * @author SYF
 */
@RestController
@RequestMapping("/job")
@Slf4j
@Api(tags = "职位接口")
public class JobController {

    @Resource
    private JobService jobService;

    @Resource
    private UserService userService;

    @Resource
    private JobCollectionService jobCollectionService;

    /**
     * 新增职位
     *
     * @param jobAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = 2)
    @ApiOperation("新增职位")
    public BaseResponse<Long> addJob(@RequestBody JobAddRequest jobAddRequest, HttpServletRequest request) {
        if (jobAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = jobService.addJob(jobAddRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 编辑职位
     *
     * @param jobUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = 2)
    @ApiOperation("编辑职位")
    public BaseResponse<Boolean> updateJob(@RequestBody JobUpdateRequest jobUpdateRequest) {
        if (jobUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = jobService.updateJob(jobUpdateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 审核职位
     *
     * @param jobIdRequest
     * @return
     */
    @PostMapping("/review")
    @AuthCheck(mustRole = 0)
    @ApiOperation("审核职位")
    public BaseResponse<Boolean> reviewJob(@RequestBody JobIdRequest jobIdRequest) {
        if (jobIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = jobService.reviewJob(jobIdRequest);
        return ResultUtils.success(result);
    }

    /**
     * 上线职位
     *
     * @param jobIdRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = 2)
    @ApiOperation("上线职位")
    public BaseResponse<Boolean> onlineJob(@RequestBody JobIdRequest jobIdRequest) {
        if (jobIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = jobService.onlineJob(jobIdRequest);
        return ResultUtils.success(result);
    }

    /**
     * 下线职位
     *
     * @param jobIdRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = 2)
    @ApiOperation("下线职位")
    public BaseResponse<Boolean> offlineJob(@RequestBody JobIdRequest jobIdRequest) {
        if (jobIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = jobService.offlineJob(jobIdRequest);
        return ResultUtils.success(result);
    }


    /**
     * 分页获取职位列表
     *
     * @param jobQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @ApiOperation("分页获取职位列表")
    public BaseResponse<Page<Job>> listJobByPage(@RequestBody JobQueryRequest jobQueryRequest, HttpServletRequest request) {
        if (jobQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        // 当前用户为招聘者时，只能查看自己公司的职位
        if (Objects.equals(loginUser.getRole(), UserRoleEnum.RECRUITER.getValue())) {
            Long companyId = loginUser.getCompanyId();
            if (companyId == null) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "当前用户未绑定企业");
            }
            jobQueryRequest.setCompanyId(loginUser.getCompanyId());
        }
        // 当前用户为求职者时，只能查看上线的职位
        if (Objects.equals(loginUser.getRole(), UserRoleEnum.CANDIDATE.getValue())) {
            jobQueryRequest.setStatus(StatusConstant.NORMAL);
        }
        long current = jobQueryRequest.getCurrent();
        long size = jobQueryRequest.getPageSize();
        Page<Job> jobPage = jobService.page(new Page<>(current, size),
                jobService.getJobQueryWrapper(jobQueryRequest));
        return ResultUtils.success(jobPage);
    }

    /**
     * 收藏职位
     *
     * @param jobIdRequest
     * @param request
     * @return
     */
    @PostMapping("/collect")
    @AuthCheck(mustRole = 1)
    @ApiOperation("收藏职位")
    public BaseResponse<Boolean> addJobCollection(@RequestBody JobIdRequest jobIdRequest, HttpServletRequest request) {
        if (jobIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = jobCollectionService.addJobCollection(jobIdRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 取消收藏职位
     *
     * @param jobIdRequest
     * @param request
     * @return
     */
    @PostMapping("/cancelCollect")
    @AuthCheck(mustRole = 1)
    @ApiOperation("取消收藏职位")
    public BaseResponse<Boolean> cancelJobCollection(@RequestBody JobIdRequest jobIdRequest, HttpServletRequest request) {
        if (jobIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = jobCollectionService.cancelJobCollection(jobIdRequest, request);
        return ResultUtils.success(result);
    }
}
