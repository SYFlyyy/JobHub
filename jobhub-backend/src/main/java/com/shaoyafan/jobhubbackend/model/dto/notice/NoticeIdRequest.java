package com.shaoyafan.jobhubbackend.model.dto.notice;

import lombok.Data;

import java.io.Serializable;

/**
 * 公告 id 请求
 *
 * @author SYF
 */
@Data
public class NoticeIdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
