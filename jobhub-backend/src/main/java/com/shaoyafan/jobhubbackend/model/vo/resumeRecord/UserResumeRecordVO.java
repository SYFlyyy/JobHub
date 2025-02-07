package com.shaoyafan.jobhubbackend.model.vo.resumeRecord;

import lombok.Data;

import java.io.Serializable;

/**
 * 求职者投递记录视图
 *
 * @author SYF
 */
@Data
public class UserResumeRecordVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 关联job表id
     */
    private Long jobId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 职位名称
     */
    private String jobName;

    /**
     * 薪资
     */
    private String salary;

    /**
     * 招聘状态
     */
    private Integer status;
}
