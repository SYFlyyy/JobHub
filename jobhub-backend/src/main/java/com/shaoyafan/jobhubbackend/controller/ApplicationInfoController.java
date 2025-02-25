package com.shaoyafan.jobhubbackend.controller;

import com.shaoyafan.jobhubbackend.annotation.AuthCheck;
import com.shaoyafan.jobhubbackend.common.BaseResponse;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.ApplicationInfo;
import com.shaoyafan.jobhubbackend.model.dto.applicationInfo.ApplicationInfoAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.applicationInfo.ApplicationInfoUpdateRequest;
import com.shaoyafan.jobhubbackend.model.dto.user.UserIdRequest;
import com.shaoyafan.jobhubbackend.service.ApplicationInfoService;
import com.shaoyafan.jobhubbackend.utils.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 在线简历接口
 *
 * @author SYF
 */
@RestController
@RequestMapping("/applicationInfo")
@Slf4j
@Api(tags = "在线简历接口")
public class ApplicationInfoController {

    @Resource
    private ApplicationInfoService applicationInfoService;

    /**
     * 新增在线简历
     *
     * @param applicationInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = 1)
    @ApiOperation("新增在线简历")
    public BaseResponse<Long> addApplicationInfo(@RequestBody ApplicationInfoAddRequest applicationInfoAddRequest, HttpServletRequest request) {
        if (applicationInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long result = applicationInfoService.addApplicationInfo(applicationInfoAddRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 更新在线简历
     *
     * @param applicationInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = 1)
    @ApiOperation("更新在线简历")
    public BaseResponse<Boolean> updateApplicationInfo(@RequestBody ApplicationInfoUpdateRequest applicationInfoUpdateRequest, HttpServletRequest request) {
        if (applicationInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = applicationInfoService.updateApplicationInfo(applicationInfoUpdateRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 查看自己在线简历
     *
     * @param request
     * @return
     */
    @PostMapping("/getOwn")
    @AuthCheck(mustRole = 1)
    @ApiOperation("查看自己在线简历")
    public BaseResponse<ApplicationInfo> getOwnApplicationInfo(HttpServletRequest request) {
        ApplicationInfo result = applicationInfoService.getOwnApplicationInfo(request);
        return ResultUtils.success(result);
    }

    /**
     * 查看求职者在线简历
     *
     * @param userIdRequest
     * @return
     */
    @PostMapping("/getUser")
    @AuthCheck(mustRole = 2)
    @ApiOperation("查看求职者在线简历")
    public BaseResponse<ApplicationInfo> getApplicationInfoByUserId(@RequestBody UserIdRequest userIdRequest) {
        if (userIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ApplicationInfo result = applicationInfoService.getApplicationInfoByUserId(userIdRequest);
        return ResultUtils.success(result);
    }

    /**
     * 查询用户是否有在线简历
     *
     * @param request
     * @return
     */
    @GetMapping("/has")
    @AuthCheck(mustRole = 1)
    @ApiOperation("查询用户是否有在线简历")
    public BaseResponse<Boolean> hasApplicationInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        ApplicationInfo applicationInfo = applicationInfoService.getById(userId);
        return ResultUtils.success(applicationInfo != null);
    }
}
