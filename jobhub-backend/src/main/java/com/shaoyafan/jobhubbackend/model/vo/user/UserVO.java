package com.shaoyafan.jobhubbackend.model.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图（脱敏）
 *
 * @author SYF
 */
@Data
public class UserVO implements Serializable {

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
     * 头像
     */
    private String avatar;

    /**
     * 角色，关联role表id
     */
    private Integer role;

    /**
     * 状态，关联status表id
     */
    private Integer status;

    /**
     * 企业，用户为招聘者时关联company_info表id
     */
    private Long companyId;

    private static final long serialVersionUID = 1L;
}
