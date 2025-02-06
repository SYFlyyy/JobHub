package com.shaoyafan.jobhubbackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;


/**
 * 用户更新个人信息请求
 *
 * @author SYF
 */
@Data
public class UserUpdateMyRequest implements Serializable {

    /**
     * 用户真实姓名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    private static final long serialVersionUID = 1L;

}
