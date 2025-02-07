package com.shaoyafan.jobhubbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaoyafan.jobhubbackend.annotation.AuthCheck;
import com.shaoyafan.jobhubbackend.common.BaseResponse;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.ResumeRecord;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.resumeRecord.ResumeRecordQueryRequest;
import com.shaoyafan.jobhubbackend.model.dto.resumeRecord.ResumeRecordUpdateStatusRequest;
import com.shaoyafan.jobhubbackend.model.vo.resumeRecord.ResumeRecordVO;
import com.shaoyafan.jobhubbackend.model.vo.resumeRecord.UserResumeRecordVO;
import com.shaoyafan.jobhubbackend.service.ResumeRecordService;
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

/**
 * 投递记录接口
 *
 * @author SYF
 */
@RestController
@RequestMapping("/resumeRecord")
@Slf4j
@Api(tags = "投递记录接口")
public class ResumeRecordController {

    @Resource
    private ResumeRecordService resumeRecordService;

    @Resource
    private UserService userService;

    /**
     * 新增投递记录
     *
     * @param jobIdRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = 1)
    @ApiOperation("新增投递记录")
    public BaseResponse<Boolean> addResumeRecord(@RequestBody JobIdRequest jobIdRequest, HttpServletRequest request) {
        if (jobIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = resumeRecordService.addResumeRecord(jobIdRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 更新招聘记录状态
     *
     * @param resumeRecordUpdateStatusRequest
     * @return
     */
    @PostMapping("/update/status")
    @AuthCheck(mustRole = 2)
    @ApiOperation("更新招聘记录状态")
    public BaseResponse<Boolean> updateResumeRecordStatus(@RequestBody ResumeRecordUpdateStatusRequest resumeRecordUpdateStatusRequest) {
        if (resumeRecordUpdateStatusRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = resumeRecordService.updateResumeRecordStatus(resumeRecordUpdateStatusRequest);
        return ResultUtils.success(result);
    }

    /**
     * 获取投递记录列表
     *
     * @param resumeRecordQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = 2)
    @ApiOperation("获取投递记录列表")
    public BaseResponse<Page<ResumeRecordVO>> listResumeRecordByPage(@RequestBody ResumeRecordQueryRequest resumeRecordQueryRequest) {
        if (resumeRecordQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = resumeRecordQueryRequest.getCurrent();
        long size = resumeRecordQueryRequest.getPageSize();
        Page<ResumeRecord> resumeRecordPage = resumeRecordService.page(new Page<>(current, size),
                resumeRecordService.getQueryWrapper(resumeRecordQueryRequest));
        Page<ResumeRecordVO> resumeRecordVOPage = resumeRecordService.getResumeRecordVOPage(resumeRecordPage);
        return ResultUtils.success(resumeRecordVOPage);
    }

    /**
     * 获取求职者投递记录列表
     *
     * @param resumeRecordQueryRequest
     * @return
     */
    @PostMapping("/list/page/my")
    @AuthCheck(mustRole = 1)
    @ApiOperation("获取求职者投递记录列表")
    public BaseResponse<Page<UserResumeRecordVO>> listMyResumeRecordByPage(@RequestBody ResumeRecordQueryRequest resumeRecordQueryRequest, HttpServletRequest request) {
        if (resumeRecordQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        resumeRecordQueryRequest.setUserId(userService.getLoginUser(request).getId());
        long current = resumeRecordQueryRequest.getCurrent();
        long size = resumeRecordQueryRequest.getPageSize();
        Page<ResumeRecord> resumeRecordPage = resumeRecordService.page(new Page<>(current, size),
                resumeRecordService.getQueryWrapper(resumeRecordQueryRequest));
        Page<UserResumeRecordVO> userResumeRecordVOPage = resumeRecordService.getUserResumeRecordVOPage(resumeRecordPage);
        return ResultUtils.success(userResumeRecordVOPage);
    }
}
