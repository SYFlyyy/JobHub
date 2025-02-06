package com.shaoyafan.jobhubbackend.model.dto.notice;

import lombok.Data;

import java.io.Serializable;

/**
 * 公告更新请求
 *
 * @author SYF
 */
@Data
public class NoticeUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告 id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;
}
