package com.shaoyafan.jobhubbackend.constant;

/**
 * 状态常量
 *
 * @author SYF
 */
public interface StatusConstant {

    String USER_LOGIN_STATE = "user_login";

    /**
     * 正常
     */
    Integer NORMAL = 0;

    /**
     * 禁用
     */
    Integer DISABLED = 1;

    /**
     * 待审核
     */
    Integer PENDING = 2;
}
