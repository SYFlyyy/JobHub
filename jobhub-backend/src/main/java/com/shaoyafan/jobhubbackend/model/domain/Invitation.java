package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 面试邀请表
 * @TableName invitation
 */
@TableName(value ="invitation")
@Data
public class Invitation implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 招聘者id
     */
    private Long recId;

    /**
     * 求职者id
     */
    private Long canId;

    /**
     * 职位id
     */
    private Long jobId;

    /**
     * 邀请信息
     */
    private String message;

    /**
     * 邀请状态（0-已发送、1-接收、2-拒绝）
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