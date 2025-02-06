package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 状态表
 * @TableName status
 */
@TableName(value ="status")
@Data
public class Status {
    /**
     * id
     */
    @TableId
    private Integer id;

    /**
     * 状态（0-正常、1-禁用、2-待审核）
     */
    private Integer status;
}