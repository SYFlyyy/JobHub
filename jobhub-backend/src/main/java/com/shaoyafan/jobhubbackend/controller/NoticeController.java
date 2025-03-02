package com.shaoyafan.jobhubbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaoyafan.jobhubbackend.annotation.AuthCheck;
import com.shaoyafan.jobhubbackend.common.BaseResponse;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.Notice;
import com.shaoyafan.jobhubbackend.model.dto.notice.NoticeAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.notice.NoticeIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.notice.NoticeQueryRequest;
import com.shaoyafan.jobhubbackend.model.dto.notice.NoticeUpdateRequest;
import com.shaoyafan.jobhubbackend.service.NoticeService;
import com.shaoyafan.jobhubbackend.utils.ResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 公告接口
 *
 * @author SYF
 */
@RestController
@RequestMapping("/notice")
@Slf4j
@Api(tags = "公告接口")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    /**
     * 添加公告
     *
     * @param noticeAddRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = 0)
    @ApiOperation("添加公告")
    public BaseResponse<Long> addNotice(@RequestBody NoticeAddRequest noticeAddRequest) {
        if (noticeAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long result = noticeService.addNotice(noticeAddRequest);
        return ResultUtils.success(result);
    }

    /**
     * 根据id获取公告
     *
     * @param noticeIdRequest
     * @return
     */
    @PostMapping("/get")
    @ApiOperation("根据id获取公告")
    public BaseResponse<Notice> getNoticeById(@RequestBody NoticeIdRequest noticeIdRequest) {
        if (noticeIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Notice notice = noticeService.getNoticeById(noticeIdRequest);
        return ResultUtils.success(notice);
    }

    /**
     * 编辑公告
     *
     * @param noticeUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = 0)
    @ApiOperation("编辑公告")
    public BaseResponse<Boolean> updateNotice(@RequestBody NoticeUpdateRequest noticeUpdateRequest) {
        if (noticeUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = noticeService.updateNotice(noticeUpdateRequest);
        return ResultUtils.success(result);
    }

    /**
     * 上线公告
     *
     * @param noticeIdRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = 0)
    @ApiOperation("上线公告")
    public BaseResponse<Boolean> onlineNotice(@RequestBody NoticeIdRequest noticeIdRequest) {
        if (noticeIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = noticeIdRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = noticeService.onlineNotice(id);
        return ResultUtils.success(result);
    }

    /**
     * 下线公告
     *
     * @param noticeIdRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = 0)
    @ApiOperation("下线公告")
    public BaseResponse<Boolean> offlineNotice(@RequestBody NoticeIdRequest noticeIdRequest) {
        if (noticeIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = noticeIdRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = noticeService.offlineNotice(id);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取公告列表
     *
     * @param noticeQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @ApiOperation("分页获取公告列表")
    public BaseResponse<Page<Notice>> listNoticeByPage(@RequestBody NoticeQueryRequest noticeQueryRequest) {
        if (noticeQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = noticeQueryRequest.getCurrent();
        long size = noticeQueryRequest.getPageSize();
        Page<Notice> noticePage = noticeService.page(new Page<>(current, size),
                noticeService.getNoticeQueryWrapper(noticeQueryRequest));
        return ResultUtils.success(noticePage);
    }
}
