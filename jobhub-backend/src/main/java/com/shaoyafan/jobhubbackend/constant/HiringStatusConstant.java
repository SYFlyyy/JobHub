package com.shaoyafan.jobhubbackend.constant;

/**
 * 投递状态常量
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
     * 已录用
     */
    Integer HIRED = 2;

    /**
     * 流程结束
     */
    Integer FINISHED = 3;
}
