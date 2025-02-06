package com.shaoyafan.jobhubbackend.model.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author SYF
 */
@Data
public class UserLoginRequest implements Serializable {

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    private static final long serialVersionUID = 1L;

}
