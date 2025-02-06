package com.shaoyafan.jobhubbackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户 id 请求
 *
 * @author SYF
 */
@Data
public class UserIdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
