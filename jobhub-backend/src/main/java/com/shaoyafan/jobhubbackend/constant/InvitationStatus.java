package com.shaoyafan.jobhubbackend.constant;

/**
 * 面试邀请状态（0-已发送、1-接受、2-拒绝）
 *
 * @author SYF
 */
public interface InvitationStatus {

    /**
     * 已发送
     */
    Integer SENT = 0;

    /**
     * 接受
     */
    Integer ACCEPTED = 1;

    /**
     * 拒绝
     */
    Integer REJECTED = 2;

}
