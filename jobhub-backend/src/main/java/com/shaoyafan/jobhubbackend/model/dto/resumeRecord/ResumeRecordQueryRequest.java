package com.shaoyafan.jobhubbackend.model.dto.resumeRecord;

import com.shaoyafan.jobhubbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 投递记录查询请求
 *
 * @author SYF
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResumeRecordQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 关联job_info表id
     */
    private Long jobId;

    /**
     * 关联user表id
     */
    private Long userId;

    /**
     * 关联resume表id
     */
    private Long resumeId;

    /**
     * 招聘状态
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
