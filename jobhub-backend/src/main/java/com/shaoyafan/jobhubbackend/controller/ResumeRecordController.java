package com.shaoyafan.jobhubbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaoyafan.jobhubbackend.annotation.AuthCheck;
import com.shaoyafan.jobhubbackend.common.BaseResponse;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.ResumeRecord;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.resumeRecord.ResumeRecordQueryRequest;
import com.shaoyafan.jobhubbackend.service.ResumeRecordService;
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
     * 分页查询投递记录
     *
     * @param resumeRecordQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = 2)
    @ApiOperation("分页查询投递记录")
    public BaseResponse<Page<ResumeRecord>> listResumeRecordByPage(@RequestBody ResumeRecordQueryRequest resumeRecordQueryRequest) {
        if (resumeRecordQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = resumeRecordQueryRequest.getCurrent();
        long size = resumeRecordQueryRequest.getPageSize();
        Page<ResumeRecord> resumeRecordPage = resumeRecordService.page(new Page<>(current, size),
                resumeRecordService.getQueryWrapper(resumeRecordQueryRequest));
        return ResultUtils.success(resumeRecordPage);
    }

}
