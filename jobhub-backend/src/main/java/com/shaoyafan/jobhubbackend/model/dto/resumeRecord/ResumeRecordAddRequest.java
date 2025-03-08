package com.shaoyafan.jobhubbackend.model.dto.resumeRecord;

import lombok.Data;

import java.io.Serializable;

/**
 * 投递记录新增请求
 *
 * @author SYF
 */
@Data
public class ResumeRecordAddRequest implements Serializable {

    /**
     *  职位 id
     */
    private Long jobId;

    /**
     *  简历槽位
     */
    private Integer slot;

    private static final long serialVersionUID = 1L;

}
