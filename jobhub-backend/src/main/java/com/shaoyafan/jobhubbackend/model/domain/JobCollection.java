package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 职位收藏表
 * @TableName job_collection
 */
@TableName(value ="job_collection")
@Data
public class JobCollection implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 关联user表id
     */
    private Long userId;

    /**
     * 关联job表id
     */
    private Long jobId;

    /**
     * 状态（0-有效、1-无效）
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