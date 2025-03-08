package com.shaoyafan.jobhubbackend.model.dto.job;

import lombok.Data;

import java.io.Serializable;

/**
 * 新增职位请求
 *
 * @author SYF
 */
@Data
public class JobAddRequest implements Serializable {

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
     * 职位详情
     */
    private String intro;

    private static final long serialVersionUID = 1L;

}
