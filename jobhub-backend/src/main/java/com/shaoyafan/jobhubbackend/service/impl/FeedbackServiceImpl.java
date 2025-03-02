package com.shaoyafan.jobhubbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shaoyafan.jobhubbackend.common.ErrorCode;
import com.shaoyafan.jobhubbackend.constant.CommonConstant;
import com.shaoyafan.jobhubbackend.exception.BusinessException;
import com.shaoyafan.jobhubbackend.model.domain.Feedback;
import com.shaoyafan.jobhubbackend.model.domain.User;
import com.shaoyafan.jobhubbackend.model.dto.feedback.FeedbackAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.feedback.FeedbackIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.feedback.FeedbackQueryRequest;
import com.shaoyafan.jobhubbackend.service.FeedbackService;
import com.shaoyafan.jobhubbackend.mapper.FeedbacckMapper;
import com.shaoyafan.jobhubbackend.service.UserService;
import com.shaoyafan.jobhubbackend.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author SYF
* @description 针对表【feedback(用户反馈表)】的数据库操作Service实现
* @createDate 2025-01-26 16:39:45
*/
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbacckMapper, Feedback>
    implements FeedbackService {

    @Resource
    private UserService userService;

    @Override
    public Long addFeedback(FeedbackAddRequest feedbackAddRequest, HttpServletRequest request) {
        Long userId = userService.getLoginUser(request).getId();
        String contact = feedbackAddRequest.getContact();
        String content = feedbackAddRequest.getContent();
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "反馈内容不能为空");
        }
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setContact(contact);
        feedback.setContent(content);
        boolean result = this.save(feedback);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return feedback.getId();
    }

    @Override
    public Feedback getFeedbackById(FeedbackIdRequest feedbackIdRequest) {
        Long id = feedbackIdRequest.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Feedback feedback = this.getById(id);
        if (feedback == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return feedback;
    }

    @Override
    public QueryWrapper<Feedback> getQueryWrapper(FeedbackQueryRequest feedbackQueryRequest) {
        if (feedbackQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = feedbackQueryRequest.getId();
        Long userId = feedbackQueryRequest.getUserId();
        String contact = feedbackQueryRequest.getContact();
        String content = feedbackQueryRequest.getContent();
        String sortField = feedbackQueryRequest.getSortField();
        String sortOrder = feedbackQueryRequest.getSortOrder();
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(userId != null, "user_id", userId);
        queryWrapper.like(StringUtils.isNotBlank(contact), "contact", contact);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }
}




