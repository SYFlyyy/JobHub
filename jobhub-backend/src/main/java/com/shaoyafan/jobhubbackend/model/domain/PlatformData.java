package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 平台数据表
 * @TableName platform_data
 */
@TableName(value ="platform_data")
@Data
public class PlatformData implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 求职者数量
     */
    private Long candidateCount;

    /**
     * 招聘者数量
     */
    private Long recruiterCount;

    /**
     * 企业数量
     */
    private Long companyCount;

    /**
     * 职位数量
     */
    private Long jobCount;

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