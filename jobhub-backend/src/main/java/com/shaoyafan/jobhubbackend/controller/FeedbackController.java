package com.shaoyafan.jobhubbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaoyafan.jobhubbackend.annotation.AuthCheck;
import com.shaoyafan.jobhubbackend.common.BaseResponse;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.Feedback;
import com.shaoyafan.jobhubbackend.model.dto.feedback.FeedbackAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.feedback.FeedbackIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.feedback.FeedbackQueryRequest;
import com.shaoyafan.jobhubbackend.service.FeedbackService;
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
 * 用户反馈接口
 *
 * @author SYF
 */
@RestController
@RequestMapping("/feedback")
@Slf4j
@Api(tags = "用户反馈接口")
public class FeedbackController {

    @Resource
    private FeedbackService feedbackService;

    /**
     * 新增反馈
     *
     * @param feedbackAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("新增反馈")
    public BaseResponse<Long> addFeedback(@RequestBody FeedbackAddRequest feedbackAddRequest, HttpServletRequest request) {
        if (feedbackAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long result = feedbackService.addFeedback(feedbackAddRequest, request);
        return ResultUtils.success(result);
    }

    /**
     * 根据id获取反馈
     *
     * @param feedbackIdRequest
     * @return
     */
    @PostMapping("/get")
    @ApiOperation("根据id获取反馈")
    public BaseResponse<Feedback> getFeedbackById(@RequestBody FeedbackIdRequest feedbackIdRequest) {
        if (feedbackIdRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Feedback feedback = feedbackService.getFeedbackById(feedbackIdRequest);
        return ResultUtils.success(feedback);
    }

    /**
     * 分页获取反馈列表
     *
     * @param feedbackQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @ApiOperation("分页获取反馈列表")
    public BaseResponse<Page<Feedback>> listFeedbackByPage(@RequestBody FeedbackQueryRequest feedbackQueryRequest) {
        if (feedbackQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = feedbackQueryRequest.getCurrent();
        long size = feedbackQueryRequest.getPageSize();
        Page<Feedback> feedbackPage = feedbackService.page(new Page<>(current, size),
                feedbackService.getQueryWrapper(feedbackQueryRequest));
        return ResultUtils.success(feedbackPage);
    }

}
