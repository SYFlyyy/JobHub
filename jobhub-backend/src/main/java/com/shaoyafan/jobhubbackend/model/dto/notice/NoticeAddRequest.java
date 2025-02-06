package com.shaoyafan.jobhubbackend.model.dto.notice;

import lombok.Data;

import java.io.Serializable;

/**
 * @author SYF
 */
@Data
public class NoticeAddRequest implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    private static final long serialVersionUID = 1L;

}
