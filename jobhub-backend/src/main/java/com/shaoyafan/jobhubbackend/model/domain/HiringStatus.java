package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 招聘状态表
 * @TableName hiring_status
 */
@TableName(value ="hiring_status")
@Data
public class HiringStatus {
    /**
     * id
     */
    @TableId
    private Integer id;

    /**
     * 招聘状态
     */
    private Object status;
}