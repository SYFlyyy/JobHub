package com.shaoyafan.jobhubbackend.model.dto.user;

import com.shaoyafan.jobhubbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户查询请求
 *
 * @author SYF
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String account;

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
     * 角色，关联role表id
     */
    private Integer role;

    /**
     * 状态，关联status表id
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
