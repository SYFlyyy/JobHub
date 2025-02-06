package com.shaoyafan.jobhubbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shaoyafan.jobhubbackend.model.domain.Feedback;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaoyafan.jobhubbackend.model.dto.feedback.FeedbackAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.feedback.FeedbackQueryRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 反馈服务
 *
* @author SYF
* @description 针对表【feedback(用户反馈表)】的数据库操作Service
* @createDate 2025-01-26 16:39:45
*/
public interface FeedbackService extends IService<Feedback> {

    /**
     * 新增反馈
     *
     * @param feedbackAddRequest
     * @param request
     * @return
     */
    Long addFeedback(FeedbackAddRequest feedbackAddRequest, HttpServletRequest request);

    /**
     * 构建查询条件
     *
     * @param feedbackQueryRequest
     * @return
     */
    QueryWrapper<Feedback> getQueryWrapper(FeedbackQueryRequest feedbackQueryRequest);

}
