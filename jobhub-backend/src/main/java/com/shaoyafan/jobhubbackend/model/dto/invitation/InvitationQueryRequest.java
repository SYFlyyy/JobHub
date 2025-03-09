package com.shaoyafan.jobhubbackend.model.dto.invitation;

import com.shaoyafan.jobhubbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 面试邀请查询请求
 *
 * @author SYF
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InvitationQueryRequest  extends PageRequest implements Serializable {

    /**
     * 招聘者id
     */
    private Long recId;

    /**
     * 求职者id
     */
    private Long canId;

    /**
     * 职位id
     */
    private Long jobId;

    /**
     * 邀请状态（0-已发送、1-接收、2-拒绝）
     */
    private Integer status;

    private static final long serialVersionUID = 1L;

}
