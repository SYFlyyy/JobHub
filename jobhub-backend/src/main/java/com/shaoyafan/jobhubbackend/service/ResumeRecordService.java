package com.shaoyafan.jobhubbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shaoyafan.jobhubbackend.model.domain.ResumeRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shaoyafan.jobhubbackend.model.dto.job.JobIdRequest;
import com.shaoyafan.jobhubbackend.model.dto.resumeRecord.ResumeRecordQueryRequest;
import com.shaoyafan.jobhubbackend.model.dto.resumeRecord.ResumeRecordUpdateStatusRequest;
import com.shaoyafan.jobhubbackend.model.vo.resumeRecord.ResumeRecordVO;
import com.shaoyafan.jobhubbackend.model.vo.resumeRecord.UserResumeRecordVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author SYF
* @description 针对表【resume_record(投递记录表)】的数据库操作Service
* @createDate 2025-01-26 16:39:45
*/
public interface ResumeRecordService extends IService<ResumeRecord> {

    /**
     * 新增投递记录
     *
     * @param jobIdRequest
     * @param request
     * @return
     */
    Boolean addResumeRecord(JobIdRequest jobIdRequest, HttpServletRequest request);

    /**
     * 更新招聘记录状态
     *
     * @param resumeRecordUpdateStatusRequest
     * @return
     */
    Boolean updateResumeRecordStatus(ResumeRecordUpdateStatusRequest resumeRecordUpdateStatusRequest);

    /**
     * 获取投递记录视图列表
     *
     * @param resumeRecordPage
     * @return
     */
    Page<ResumeRecordVO> getResumeRecordVOPage(Page<ResumeRecord> resumeRecordPage);

    /**
     * 获取求职者投递记录视图列表
     *
     * @param resumeRecordPage
     * @return
     */
    Page<UserResumeRecordVO> getUserResumeRecordVOPage(Page<ResumeRecord> resumeRecordPage);

    /**
     * 获取查询条件
     *
     * @param resumeRecordQueryRequest
     * @return
     */
    QueryWrapper<ResumeRecord> getQueryWrapper(ResumeRecordQueryRequest resumeRecordQueryRequest);

}
