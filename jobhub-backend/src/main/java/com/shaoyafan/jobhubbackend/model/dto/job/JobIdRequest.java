package com.shaoyafan.jobhubbackend.model.dto.job;

import lombok.Data;

import java.io.Serializable;

/**
 * 职位 id 请求
 *
 * @author SYF
 */
@Data
public class JobIdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
