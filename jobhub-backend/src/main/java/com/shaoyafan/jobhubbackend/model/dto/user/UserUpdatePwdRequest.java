package com.shaoyafan.jobhubbackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户修改密码请求
 *
 * @author SYF
 */
@Data
public class UserUpdatePwdRequest implements Serializable {

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认密码
     */
    private String checkPassword;

    private static final long serialVersionUID = 1L;

}
