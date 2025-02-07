package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 职位表
 * @TableName job
 */
@TableName(value ="job")
@Data
public class Job implements Serializable {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 企业，关联user表company_id
     */
    private Long companyId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 职位名称
     */
    private String name;

    /**
     * 职位类型（0-全职、1-实习、2-兼职）
     */
    private Integer type;

    /**
     * 薪资
     */
    private String salary;

    /**
     * 职位详情
     */
    private String intro;

    /**
     * 状态（0-上线、1-下线）
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