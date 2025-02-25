package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 投递记录表
 * @TableName resume_record
 */
@TableName(value ="resume_record")
@Data
public class ResumeRecord implements Serializable {

    /**
     * id
     */
    @TableId
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
     * 关联resume表id
     */
    private Long resumeId;

    /**
     * 招聘状态(0-已投递、1-面试中、2-面试通过、3-录用意向、4-已录用、5-流程结束)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}