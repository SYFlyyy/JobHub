package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 招聘数据表
 * @TableName hiring_data
 */
@TableName(value ="hiring_data")
@Data
public class HiringData {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 职位，关联job表id
     */
    private Long jobId;

    /**
     * 查看量
     */
    private Long viewCount;

    /**
     * 投递量
     */
    private Long applicationCount;

    /**
     * 收藏量
     */
    private Long favoriteCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}