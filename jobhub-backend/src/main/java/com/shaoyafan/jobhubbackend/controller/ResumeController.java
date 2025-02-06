package com.shaoyafan.jobhubbackend.controller;

import com.shaoyafan.jobhubbackend.annotation.AuthCheck;
import com.shaoyafan.jobhubbackend.common.BaseResponse;
import com.shaoyafan.jobhubbackend.model.dto.user.UserIdRequest;
import com.shaoyafan.jobhubbackend.service.ResumeService;
import com.shaoyafan.jobhubbackend.utils.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 简历附件接口
 *
 * @author SYF
 */
@RestController
@RequestMapping("/resume")
@Slf4j
@Api(tags = "简历附件接口")
public class ResumeController {

    @Resource
    private ResumeService resumeService;

    /**
     * 上传简历附件
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/upload")
    @AuthCheck(mustRole = 1)
    @ApiOperation("上传简历附件")
    public BaseResponse<Boolean> uploadResume(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Boolean result = resumeService.uploadResume(file, request);
        return ResultUtils.success(result);
    }

    /**
     * 下载简历附件
     *
     * @param userIdRequest
     * @return
     */
    @PostMapping("/download")
    @AuthCheck(mustRole = 2)
    @ApiOperation(value = "下载简历附件", produces = "application/octet-stream")
    public ResponseEntity<org.springframework.core.io.Resource> downloadResume(@RequestBody UserIdRequest userIdRequest) {
        return resumeService.downloadResume(userIdRequest);
    }

    /**
     * 删除简历附件
     *
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = 1)
    @ApiOperation("删除简历附件")
    public BaseResponse<Boolean> deleteResume(HttpServletRequest request) {
        Boolean result = resumeService.deleteResume(request);
        return ResultUtils.success(result);
    }
}
