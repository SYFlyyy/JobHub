package com.shaoyafan.jobhubbackend.model.dto.resume;

import lombok.Data;

import java.io.Serializable;

/**
 * 简历附件id请求
 *
 * @author SYF
 */
@Data
public class ResumeIdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;

}
