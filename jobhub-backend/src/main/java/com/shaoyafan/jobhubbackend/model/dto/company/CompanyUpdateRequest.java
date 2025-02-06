package com.shaoyafan.jobhubbackend.model.dto.company;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业更新请求
 *
 * @author SYF
 */
@Data
public class CompanyUpdateRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;

}
