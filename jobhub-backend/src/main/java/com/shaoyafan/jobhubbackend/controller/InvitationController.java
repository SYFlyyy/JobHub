package com.shaoyafan.jobhubbackend.controller;

import com.shaoyafan.jobhubbackend.annotation.AuthCheck;
import com.shaoyafan.jobhubbackend.common.BaseResponse;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.dto.invitation.InvitationAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.invitation.InvitationIdRequest;
import com.shaoyafan.jobhubbackend.service.InvitationService;
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

/**
 * 面试邀请接口
 *
 * @author SYF
 */
@RestController
@RequestMapping("/invitation")
@Slf4j
@Api(tags = "面试邀请接口")
public class InvitationController {

    @Resource
    private InvitationService invitationService;

    /**
     * 新增面试邀请
     *
     * @param invitationAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = 2)
    @ApiOperation("新增面试邀请")
    public BaseResponse<Boolean> addInvitation(@RequestBody InvitationAddRequest invitationAddRequest, HttpServletRequest request) {
        if (invitationAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = invitationService.addInvitation(invitationAddRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 接受面试邀请
     *
     * @param invitationIdRequest
     * @return
     */
    @PostMapping("/accept")
    @AuthCheck(mustRole = 1)
    @ApiOperation("接受面试邀请")
    public BaseResponse<Boolean> acceptInvitation(@RequestBody InvitationIdRequest invitationIdRequest, HttpServletRequest request) {
        if (invitationIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = invitationService.acceptInvitation(invitationIdRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 拒绝面试邀请
     *
     * @param invitationIdRequest
     * @return
     */
    @PostMapping("/reject")
    @AuthCheck(mustRole = 1)
    @ApiOperation("拒绝面试邀请")
    public BaseResponse<Boolean> rejectInvitation(@RequestBody InvitationIdRequest invitationIdRequest, HttpServletRequest request) {
        if (invitationIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = invitationService.rejectInvitation(invitationIdRequest, request);
        return ResultUtils.success(result);
    }
}
