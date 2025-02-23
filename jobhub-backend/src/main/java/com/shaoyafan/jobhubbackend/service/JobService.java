package com.shaoyafan.jobhubbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaoyafan.jobhubbackend.model.domain.Job;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaoyafan.jobhubbackend.model.dto.job.JobAddRequest;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.job.JobQueryRequest;
import com.shaoyafan.jobhubbackend.model.dto.job.JobUpdateRequest;
import com.shaoyafan.jobhubbackend.model.vo.job.JobVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 职位服务
 *
* @author SYF
* @description 针对表【job(职位表)】的数据库操作Service
* @createDate 2025-01-26 16:39:45
*/
public interface JobService extends IService<Job> {

    /**
     * 新增职位
     *
     * @param jobAddRequest
     * @param request
     * @return
     */
    Long addJob(JobAddRequest jobAddRequest, HttpServletRequest request);

    /**
     * 编辑职位
     *
     * @param jobUpdateRequest
     * @return
     */
    Boolean updateJob(JobUpdateRequest jobUpdateRequest);

    /**
     * 审核职位
     *
     * @param jobIdRequest
     * @return
     */
    Boolean reviewJob(JobIdRequest jobIdRequest);

    /**
     * 上线职位
     *
     * @param jobIdRequest
     * @return
     */
    Boolean onlineJob(JobIdRequest jobIdRequest);

    /**
     * 下线职位
     *
     * @param jobIdRequest
     * @return
     */
    Boolean offlineJob(JobIdRequest jobIdRequest);

    /**
     * 获取职位（含企业信息）
     *
     * @param jobQueryRequest
     * @return
     */
    Page<JobVO> listJobWithCompany(JobQueryRequest jobQueryRequest);

    /**
     * 获取查询条件
     *
     * @param jobQueryRequest
     * @return
     */
    QueryWrapper<Job> getJobQueryWrapper(JobQueryRequest jobQueryRequest);

    /**
     * 获取查询条件
     *
     * @param jobQueryRequest
     * @return
     */
    QueryWrapper<Job> getJobVOQueryWrapper(JobQueryRequest jobQueryRequest);

    /**
     * 获取收藏的职位列表
     *
     * @param request
     * @return
     */
    Page<JobVO> getCollectedJobByPage(JobQueryRequest jobQueryRequest, HttpServletRequest request);

}
