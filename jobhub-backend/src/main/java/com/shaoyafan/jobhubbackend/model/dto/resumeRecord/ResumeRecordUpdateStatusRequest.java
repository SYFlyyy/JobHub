package com.shaoyafan.jobhubbackend.model.dto.resumeRecord;

import lombok.Data;

import java.io.Serializable;

/**
 * 招聘状态更新请求
 *
 * @author SYF
 */
@Data
public class ResumeRecordUpdateStatusRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 招聘状态
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
