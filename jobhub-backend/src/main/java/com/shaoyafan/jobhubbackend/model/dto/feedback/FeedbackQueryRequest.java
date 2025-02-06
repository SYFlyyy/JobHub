package com.shaoyafan.jobhubbackend.model.dto.feedback;

import com.shaoyafan.jobhubbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 反馈查询请求
 *
 * @author SYF
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FeedbackQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 关联user表id
     */
    private Long userId;

    /**
     * 联系方式（手机或邮箱）
     */
    private String contact;

    /**
     * 反馈内容
     */
    private String content;

    private static final long serialVersionUID = 1L;

}
