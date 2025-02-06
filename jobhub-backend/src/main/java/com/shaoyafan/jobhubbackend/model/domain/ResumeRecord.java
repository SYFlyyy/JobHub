package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 投递记录表
 * @TableName resume_record
 */
@TableName(value ="resume_record")
@Data
public class ResumeRecord {
    /**
     * id
     */
    @TableId
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
    private Object status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}