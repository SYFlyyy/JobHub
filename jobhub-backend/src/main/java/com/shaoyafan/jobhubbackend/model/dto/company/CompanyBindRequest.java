package com.shaoyafan.jobhubbackend.model.dto.company;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业绑定请求
 *
 * @author SYF
 */
@Data
public class CompanyBindRequest implements Serializable {

    /**
     * 企业名称
     */
    private String name;

    private static final long serialVersionUID = 1L;
}
