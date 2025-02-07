package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 招聘数据表
 * @TableName hiring_data
 */
@TableName(value ="hiring_data")
@Data
public class HiringData {
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
     * 投递量
     */
    private Long applicationCount;

    /**
     * 收藏量
     */
    private Long favoriteCount;

    /**
     * 面试候选人数量
     */
    private Long interviewCount;

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
    private Date updateTime;
}