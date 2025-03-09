package com.shaoyafan.jobhubbackend.model.vo.hiringData;

import lombok.Data;

import java.io.Serializable;

/**
 * 招聘数据视图
 *
 * @author SYF
 */
@Data
public class HiringDataVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 职位，关联job表id
     */
    private Long jobId;

    /**
     * 收藏量
     */
    private Long favoriteCount;

    /**
     * 投递量
     */
    private Long applicationCount;

    /**
     * 简历初筛通过量（发送面试邀请数量）
     */
    private Long applicationPassCount;

    /**
     * 面试候选人数量
     */
    private Long interviewCount;

    /**
     * 面试通过数量
     */
    private Long interviewPassCount;

    /**
     * 意向数量
     */
    private Long intentionCount;

    /**
     * 录用数量
     */
    private Long hiredCount;

    /**
     * 简历初筛通过率（applicationPassCount/applicationCount）
     */
    private String applicationPassRate;

    /**
     * 面试通过率（interviewPassCount/interviewCount）
     */
    private String interviewPassRate;

    /**
     * 最终录用率（hiredCount/intentionCount）
     */
    private String hiredPassRate;

    /**
     * 全流程转化率（hiredCount / applicationCount）
     */
    private String totalConversionRate;

    private static final long serialVersionUID = 1L;

}
