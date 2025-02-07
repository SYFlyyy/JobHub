package com.shaoyafan.jobhubbackend.model.dto.applicationInfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 在线简历新增请求
 *
 * @author SYF
 */
@Data
public class ApplicationInfoAddRequest implements Serializable {

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别（0-男、1-女）
     */
    private Integer gender;

    /**
     * 出生日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @JsonFormat(pattern = "yyyy-MM-dd")
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

    private static final long serialVersionUID = 1L;

}
