package com.shaoyafan.jobhubbackend.model.dto.notice;

import com.shaoyafan.jobhubbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 公告查询请求
 *
 * @author SYF
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NoticeQueryRequest extends PageRequest implements Serializable {

    /**
     * id
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

    /**
     * 状态（0-发布、1-下线）
     */
    private Integer status;

    private static final long serialVersionUID = 1L;

}
