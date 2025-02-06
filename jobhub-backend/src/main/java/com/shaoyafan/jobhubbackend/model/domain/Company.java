package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 企业信息表
 * @TableName company
 */
@TableName(value ="company")
@Data
public class Company implements Serializable {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 企业介绍
     */
    private String intro;

    /**
     * 地址
     */
    private String address;

    /**
     * 企业类型
     */
    private String type;

    /**
     * 企业规模
     */
    private String size;

    /**
     * 企业logo
     */
    private String logo;

    /**
     * 状态，关联status表id
     */
    private Integer status;

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