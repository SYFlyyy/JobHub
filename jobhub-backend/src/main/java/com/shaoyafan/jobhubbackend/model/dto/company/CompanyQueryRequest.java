package com.shaoyafan.jobhubbackend.model.dto.company;

import com.shaoyafan.jobhubbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 企业查询请求
 *
 * @author SYF
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 企业介绍
     */
    private String intro;

    /**
     * 地址
     */
    private String address;

    /**
     * 企业类型
     */
    private String type;

    /**
     * 企业规模
     */
    private String size;

    /**
     * 企业logo
     */
    private String logo;

    /**
     * 状态，关联status表id
     */
    private Integer status;

    private static final long serialVersionUID = 1L;;
}
