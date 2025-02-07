package com.shaoyafan.jobhubbackend.model.vo.resumeRecord;

import lombok.Data;

import java.io.Serializable;

/**
 * 投递记录视图
 *
 * @author SYF
 */
@Data
public class ResumeRecordVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 关联job表id
     */
    private Long jobId;

    /**
     * 关联user表id
     */
    private Long userId;

    /**
     * 求职者姓名
     */
    private String userName;

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
