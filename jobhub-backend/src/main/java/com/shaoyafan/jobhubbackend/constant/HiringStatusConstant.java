package com.shaoyafan.jobhubbackend.constant;

/**
 * 投递状态常量(0-已投递、1-面试中、2-面试通过、3-录用意向、4-已录用、5-流程结束)
 *
 * @author SYF
 */
public interface HiringStatusConstant {

    /**
     * 已投递
     */
    Integer APPLIED = 0;

    /**
     * 面试中
     */
    Integer INTERVIEWING = 1;

    /**
     * 面试通过
     */
    Integer PASSED = 2;

    /**
     * 录用意向
     */
    Integer INTENTION = 3;

    /**
     * 已录用
     */
    Integer HIRED = 4;

    /**
     * 流程结束
     */
    Integer FINISHED = 5;
}
