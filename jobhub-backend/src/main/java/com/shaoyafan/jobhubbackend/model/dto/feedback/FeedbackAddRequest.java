package com.shaoyafan.jobhubbackend.model.dto.feedback;

import lombok.Data;

/**
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
