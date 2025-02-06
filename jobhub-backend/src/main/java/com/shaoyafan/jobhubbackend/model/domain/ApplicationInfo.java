package com.shaoyafan.jobhubbackend.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 在线简历表
 * @TableName application_info
 */
@TableName(value ="application_info")
@Data
public class ApplicationInfo {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 关联user表id
     */
    private Long userId;

    /**
     * 性别（0-男、1-女）
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private Date birthDate;

    /**
     * 手机
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 学校
     */
    private String school;

    /**
     * 专业
     */
    private String major;

    /**
     * 毕业时间
     */
    private Date graduation;

    /**
     * 荣誉证书
     */
    private String honor;

    /**
     * 专业技能
     */
    private String skill;

    /**
     * 项目经历
     */
    private String projectExperience;

    /**
     * 工作/实习经历
     */
    private String workExperience;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}