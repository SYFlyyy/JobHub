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
     * 职位名称
     */
    private String name;

    /**
     * 职位类型（0-全职、1-实习、2-兼职）
     */
    private Integer type;

    /**
     * 薪资范围下限
     */
    private Integer minSalary;

    /**
     * 薪资范围上限
     */
    private Integer maxSalary;

    /**
     * 薪资单位（0-元、1-千元/k、2-万元/w）
     */
    private Integer salaryUnit;

    /**
     * 结算周期单位（0-小时、1-天、2-月、3-年）
     */
    private Integer periodUnit;

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