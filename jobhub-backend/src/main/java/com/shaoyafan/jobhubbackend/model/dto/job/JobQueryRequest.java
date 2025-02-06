package com.shaoyafan.jobhubbackend.model.dto.job;

import com.shaoyafan.jobhubbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 职位查询请求
 *
 * @author SYF
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JobQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
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
