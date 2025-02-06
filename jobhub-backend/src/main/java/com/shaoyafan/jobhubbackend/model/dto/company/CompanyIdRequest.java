package com.shaoyafan.jobhubbackend.model.dto.company;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业 id 请求
 *
 * @author SYF
 */
@Data
public class CompanyIdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
