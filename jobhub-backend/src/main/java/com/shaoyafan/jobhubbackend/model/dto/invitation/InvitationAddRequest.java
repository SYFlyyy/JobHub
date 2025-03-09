package com.shaoyafan.jobhubbackend.model.dto.invitation;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 面试邀请新增请求
 *
 * @author SYF
 */
@Data
public class InvitationAddRequest implements Serializable {

    /**
     * 求职者id
     */
    private Long canId;

    /**
     * 职位id
     */
    private Long jobId;

    /**
     * 邀请信息
     */
    private String message;

    private static final long serialVersionUID = 1L;

}
