package com.shaoyafan.jobhubbackend.model.dto.invitation;

import lombok.Data;

import java.io.Serializable;

/**
 * 面试邀请id请求
 *
 * @author SYF
 */
@Data
public class InvitationIdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
