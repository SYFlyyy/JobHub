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
     * 薪资
     */
    private String salary;

    /**
     * 职位详情
     */
    private String intro;

    private static final long serialVersionUID = 1L;

}
