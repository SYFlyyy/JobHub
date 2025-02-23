package com.shaoyafan.jobhubbackend.model.vo.job;

import lombok.Data;

/**
 * 职位视图
 *
 * @author SYF
 */
@Data
public class JobVO {

    /**
     * id
     */
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
     * 企业规模
     */
    private String size;

    /**
     * 企业地址
     */
    private String address;

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

    private static final long serialVersionUID = 1L;
}
