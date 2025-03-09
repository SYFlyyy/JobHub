package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 招聘数据表
 * @TableName hiring_data
 */
@TableName(value ="hiring_data")
@Data
public class HiringData implements Serializable {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 职位，关联job表id
     */
    private Long jobId;

    /**
     * 收藏量
     */
    private Long favoriteCount;

    /**
     * 投递量
     */
    private Long applicationCount;

    /**
     * 简历初筛通过量（发送面试邀请数量）
     */
    private Long applicationPassCount;

    /**
     * 面试候选人数量
     */
    private Long interviewCount;

    /**
     * 面试通过数量
     */
    private Long interviewPassCount;

    /**
     * 意向数量
     */
    private Long intentionCount;

    /**
     * 录用数量
     */
    private Long hiredCount;

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