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
     * 投递量
     */
    private Long applicationCount;

    /**
     * 收藏量
     */
    private Long favoriteCount;

    /**
     * 面试候选人数量
     */
    private Long interviewCount;

    /**
     * 录用数量
     */
    private Long hiredCount;

    /**
     * 面试转换率
     */
    private String InterviewRate;

    private static final long serialVersionUID = 1L;

}
