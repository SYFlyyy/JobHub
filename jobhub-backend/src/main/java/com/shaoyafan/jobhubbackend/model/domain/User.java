package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}