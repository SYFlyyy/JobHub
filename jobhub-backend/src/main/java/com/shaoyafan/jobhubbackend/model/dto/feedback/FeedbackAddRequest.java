package com.shaoyafan.jobhubbackend.model.dto.feedback;

import lombok.Data;

/**
 * 反馈新增请求
 *
 * @author SYF
 */
@Data
public class FeedbackAddRequest {

    /**
     * 联系方式（手机或邮箱）
     */
    private String contact;

    /**
     * 反馈内容
     */
    private String content;
}
