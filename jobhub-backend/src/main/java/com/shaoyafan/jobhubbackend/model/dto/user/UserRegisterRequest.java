package com.shaoyafan.jobhubbackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author SYF
 */
@Data
public class UserRegisterRequest implements Serializable {

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 确认密码
     */
    private String checkPassword;

    /**
     * 角色，关联role表id
     */
    private Integer role;

    private static final long serialVersionUID = 1L;

}
