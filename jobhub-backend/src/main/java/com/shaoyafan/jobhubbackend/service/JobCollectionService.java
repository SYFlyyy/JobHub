package com.shaoyafan.jobhubbackend.service;

import com.shaoyafan.jobhubbackend.model.domain.JobCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;

import javax.servlet.http.HttpServletRequest;

/**
* @author SYF
* @description 针对表【job_collection(职位收藏表)】的数据库操作Service
* @createDate 2025-01-26 16:39:45
*/
public interface JobCollectionService extends IService<JobCollection> {

    /**
     * 收藏职位
     *
     * @param jobIdRequest
     * @param request
     * @return
     */
    Boolean addJobCollection(JobIdRequest jobIdRequest, HttpServletRequest request);

    /**
     * 取消收藏职位
     *
     * @param jobIdRequest
     * @param request
     * @return
     */
    Boolean cancelJobCollection(JobIdRequest jobIdRequest, HttpServletRequest request);
}
