package com.shaoyafan.jobhubbackend.controller;

import com.shaoyafan.jobhubbackend.annotation.AuthCheck;
import com.shaoyafan.jobhubbackend.common.BaseResponse;
import com.shaoyafan.jobhubbackend.model.domain.Resume;
import com.shaoyafan.jobhubbackend.model.dto.resume.ResumeIdRequest;
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
import java.util.List;

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
    public BaseResponse<Boolean> uploadResume(@RequestParam("file") MultipartFile file, @RequestParam("slot") Integer slot,  HttpServletRequest request) {
        Boolean result = resumeService.uploadResume(file, slot, request);
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

    /**
     * 返回简历附件路径
     *
     * @param request
     * @return
     */
    @GetMapping("/userPath")
    @ApiOperation("获取简历附件路径")
    public BaseResponse<List<Resume>> getUserResumePath(HttpServletRequest request) {
        List<Resume> userResumePath = resumeService.getUserResumePath(request);
        return ResultUtils.success(userResumePath);
    }

    /**
     * 招聘者获取简历附件路径
     *
     * @param resumeIdRequest
     * @return
     */
    @PostMapping("/path")
    @ApiOperation("招聘者获取简历附件路径")
    public BaseResponse<String> getResumePath(@RequestBody ResumeIdRequest resumeIdRequest) {
        String resumePath = resumeService.getResumePath(resumeIdRequest);
        return ResultUtils.success(resumePath);
    }
}
